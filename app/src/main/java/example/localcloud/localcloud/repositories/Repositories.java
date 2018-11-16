package example.localcloud.localcloud.repositories;

import example.localcloud.localcloud.components.BaseComponent;
import example.localcloud.localcloud.components.DatabasesComponent;

public class Repositories extends BaseRepository {

    public FilesRepository files() {
        return (FilesRepository) BaseRepository.getOrCreate(FilesRepository.class);
    }
}
