package com.koheisaito.service;

import java.util.List;

import com.koheisaito.repository.GetFormRecognizeResultRepository;

public class GetFormRecognizeResultService {

    private GetFormRecognizeResultRepository getFormRecognizeResultRepository;

    public GetFormRecognizeResultService(GetFormRecognizeResultRepository getFormRecognizeResultRepository) {
        this.getFormRecognizeResultRepository = getFormRecognizeResultRepository;
    }

    public List<String> getFormRecognizeResult(List<String> key) {
        return getFormRecognizeResultRepository.getFormRecognizeResult(key);
    }
}
