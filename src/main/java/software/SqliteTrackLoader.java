package software;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SqliteTrackLoader implements TrackLoader{

    private final Connection connection;
    public SqliteTrackLoader(Connection connection) {
        this.connection= connection;
    }

    private final static String querryAllSql = "SELECT tracks.name AS track, composer, milliseconds, title, genres.name AS genre, artists.name AS artist FROM tracks, albums, genres, artists WHERE\n" +
            "   tracks.AlbumId = albums.AlbumId AND\n" +
            "   tracks.GenreId = genres.GenreId AND\n" +
            "   albums.ArtistId = artists.ArtistId";


    @Override
    public List<Track> loadAll() {
        try {
            return load(resultSetOf(querryAllSql));
        } catch (SQLException e){
            return Collections.emptyList();
        }

    }

    private List<Track> load(ResultSet resultSet) throws SQLException{
        List<Track> list = new ArrayList<>();
        while (resultSet.next()) list.add(trackFrom(resultSet));
        return list;
    }

    private Track trackFrom(ResultSet resultSet) throws SQLException{
        return new Track(
                resultSet.getString("Track"),
                resultSet.getString("composer"),
                resultSet.getInt("milliseconds"),
                resultSet.getString("title"),
                resultSet.getString("genre"),
                resultSet.getString("artist")
        );
    }

    private ResultSet resultSetOf(String sql) throws SQLException {
        return connection.createStatement().executeQuery(sql);
    }
}
