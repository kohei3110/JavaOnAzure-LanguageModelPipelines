package com.koheisaito.service;

import java.util.List;

import com.koheisaito.repository.DeleteBlobRepository;

public class DeleteBlobService {

    private DeleteBlobRepository deleteBlobRepository;

    public DeleteBlobService(DeleteBlobRepository deleteBlobRepository) {
        this.deleteBlobRepository = deleteBlobRepository;
    }

    public void deleteBlobs(List<String> blobs) throws Exception {
        deleteBlobRepository.deleteBlobs(blobs);
    }
}
