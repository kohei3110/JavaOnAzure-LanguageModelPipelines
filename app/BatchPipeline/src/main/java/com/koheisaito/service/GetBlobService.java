package com.koheisaito.service;

import java.util.List;

import com.koheisaito.repository.GetBlobRepository;

public class GetBlobService {

    private GetBlobRepository getBlobRepository;

    public GetBlobService(GetBlobRepository getBlobRepository) {
        this.getBlobRepository = getBlobRepository;
    }

    public List<String> getBlob() throws Exception {
        return getBlobRepository.getBlob();
    }
}
