package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getAll() {
        String sqlQuery = "SELECT * FROM films "
                + "JOIN rating_mpa ON films.rating_id = rating_mpa.rating_id "
                + "LEFT JOIN film_genres ON film_genres.film_id = films.film_id "
                + "LEFT JOIN genres ON genres.genre_id = film_genres.genre_id";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::filmMapRs);
        return addGenreForList(films);
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("film_name", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("duration", film.getDuration());
        parameters.put("release_date", java.sql.Date.valueOf(film.getReleaseDate()));
        parameters.put("rating_id", film.getMpa().getId());
        Number id = insert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        addGenres(id.longValue(), film.getGenres());
        return Film.builder()
                .id(id.longValue())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .genres(film.getGenres())
                .mpa(film.getMpa())
                .build();
    }

    private void addGenres(Long filmId, Set<Genre> genres) {
        deleteAllGenresById(filmId);
        if (genres == null || genres.isEmpty()) {
            return;
        }
        String sqlQuery = "INSERT INTO film_genres (film_id, genre_id) "
                + "VALUES (?, ?)";
        List<Genre> genresTable = new ArrayList<>(genres);
        this.jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, Math.toIntExact(filmId));
                ps.setInt(2, Math.toIntExact(genresTable.get(i).getId()));
            }

            public int getBatchSize() {
                return genresTable.size();
            }
        });
    }

    private void deleteAllGenresById(Long filmId) {
        String sglQuery = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sglQuery, filmId);
    }

    @Override
    public Film put(Film film) {
        String sqlQuery = "UPDATE films "
                + "SET film_name = ?, "
                + "description = ?, "
                + "duration = ?, "
                + "release_date = ?, "
                + "rating_id = ? "
                + "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getDuration(),
                film.getReleaseDate(), film.getMpa().getId(), film.getId());
        addGenres(film.getId(), film.getGenres());
        Long filmId = film.getId();
        film.setGenres(getGenres(filmId));
        return getFilm(filmId);
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        String sqlQuery = "INSERT INTO likes (film_id, user_id) "
                + "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return getFilm(filmId);
    }

    @Override
    public Film removeLike(Long filmId, Long userId) {
        String sqlQuery = "DELETE FROM likes "
                + "WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return getFilm(filmId);
    }

    @Override
    public Collection<Film> getPopularFilm(Integer count) {
        String sqlQuery = "SELECT * FROM films "
                + "LEFT JOIN likes ON likes.film_id = films.film_id "
                + "JOIN rating_mpa ON films.rating_id = rating_mpa.rating_id "
                + "GROUP BY films.film_id "
                + "ORDER BY COUNT (likes.film_id) DESC "
                + "LIMIT "
                + count;
        return jdbcTemplate.query(sqlQuery, this::filmMapRs);
    }

    @Override
    public Film getFilm(Long filmId) {
        String sqlQuery = "SELECT * FROM films "
                + "JOIN rating_mpa ON films.rating_id = rating_mpa.rating_id "
                + "WHERE film_id = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        if (srs.next()) {
            return filmMapSrs(srs);
        } else {
            throw new IncorrectParameterException("Фильм с ID = " + filmId + " не найден!");
        }
    }

    private Film filmMapSrs(SqlRowSet srs) {
        Long id = srs.getLong("film_id");
        String name = srs.getString("film_name");
        String description = srs.getString("description");
        int duration = srs.getInt("duration");
        LocalDate releaseDate = Objects.requireNonNull(srs.getTimestamp("release_date"))
                .toLocalDateTime().toLocalDate();
        Long mpaId = srs.getLong("rating_id");
        String mpaName = srs.getString("rating_name");
        RatingMpa mpa = RatingMpa.builder()
                .id(mpaId)
                .name(mpaName)
                .build();
        Set<Genre> genres = getGenres(id);
        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .duration(duration)
                .mpa(mpa)
                .genres(genres)
                .releaseDate(releaseDate)
                .build();
    }

    private Film filmMapRs(ResultSet rs, int id) throws SQLException {
        Long filmId = rs.getLong("film_id");
        String name = rs.getString("film_name");
        String description = rs.getString("description");
        int duration = rs.getInt("duration");
        LocalDate releaseDate = rs.getTimestamp("release_date").toLocalDateTime().toLocalDate();
        Long mpaId = rs.getLong("rating_id");
        String mpaName = rs.getString("rating_name");
        RatingMpa mpa = RatingMpa.builder()
                .id(mpaId)
                .name(mpaName)
                .build();
        Set<Genre> genres = new HashSet<>();
        return Film.builder()
                .id(filmId)
                .name(name)
                .description(description)
                .duration(duration)
                .genres(genres)
                .mpa(mpa)
                .releaseDate(releaseDate)
                .build();
    }

    private Set<Genre> getGenres(Long filmId) {
        Comparator<Genre> compId = Comparator.comparing(Genre::getId);
        Set<Genre> genres = new TreeSet<>(compId);
        String sqlQuery = "SELECT film_genres.genre_id, genres.genre_name FROM film_genres "
                + "JOIN genres ON genres.genre_id = film_genres.genre_id "
                + "WHERE film_id = ? ORDER BY genre_id ASC";
        genres.addAll(jdbcTemplate.query(sqlQuery, this::genreMap, filmId));
        return genres;
    }

    private Genre genreMap(ResultSet rs, int id) throws SQLException {
        Long genreId = rs.getLong("genre_id");
        String genreName = rs.getString("genre_name");
        return Genre.builder()
                .id(genreId)
                .name(genreName)
                .build();
    }

    private List<Film> addGenreForList(List<Film> films) {
        Map<Long, Film> filmsTable = films.stream().collect(Collectors.toMap(Film::getId, film -> film));
        jdbcTemplate.execute("CREATE TEMPORARY TABLE IF NOT EXISTS temp_table (id INT NOT NULL)");
        List<Object[]> employeeIds = new ArrayList<>();
        for (Long id : filmsTable.keySet()) {
            employeeIds.add(new Object[]{id});
        }
        String name = "temp_table";
        jdbcTemplate.batchUpdate("INSERT INTO " + name + " VALUES(?)", employeeIds);
        final String sqlQuery =
                "SELECT * " +
                        "FROM film_genres " +
                        "LEFT OUTER JOIN genres ON film_genres.genre_id = genres.genre_id " +
                        "WHERE film_genres.film_id IN (SELECT id FROM " + name + ") " +
                        "ORDER BY film_genres.genre_id";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            filmsTable.get(rs.getLong("film_id"))
                    .addGenre(Genre.builder()
                            .id(rs.getLong("genre_id"))
                            .name(rs.getString("genre_name"))
                            .build());
        });
        jdbcTemplate.execute("DROP TABLE " + name);
        return films;
    }
}
