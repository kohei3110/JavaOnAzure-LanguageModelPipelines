package com.koheisaito;

import com.koheisaito.repository.CreateMessageEmbeddingRepository;
import com.koheisaito.repository.DeleteBlobRepository;
import com.koheisaito.repository.GetBlobRepository;
import com.koheisaito.repository.GetFormRecognizeResultRepository;
import com.koheisaito.service.CreateMessageEmbeddingService;
import com.koheisaito.service.DeleteBlobService;
import com.koheisaito.service.GetBlobService;
import com.koheisaito.service.GetFormRecognizeResultService;

public class Factory {

    private CreateMessageEmbeddingRepository createMessageEmbeddingRepository;
    private CreateMessageEmbeddingService createMessageEmbeddingService;
    private GetBlobRepository getBlobRepository;
    private GetBlobService getBlobService;
    private GetFormRecognizeResultRepository getFormRecognizeResultRepository;
    private GetFormRecognizeResultService getFormRecognizeResultService;
    private DeleteBlobRepository deleteBlobRepository;
    private DeleteBlobService deleteBlobService;

    public Factory() {
        this.createMessageEmbeddingRepository = new CreateMessageEmbeddingRepository();
        this.createMessageEmbeddingService = new CreateMessageEmbeddingService(createMessageEmbeddingRepository);
        this.getBlobRepository = new GetBlobRepository();
        this.getBlobService = new GetBlobService(this.getBlobRepository);
        this.getFormRecognizeResultRepository = new GetFormRecognizeResultRepository();
        this.getFormRecognizeResultService = new GetFormRecognizeResultService(this.getFormRecognizeResultRepository);
        this.deleteBlobRepository = new DeleteBlobRepository();
        this.deleteBlobService = new DeleteBlobService(this.deleteBlobRepository);
    }

    public CreateMessageEmbeddingService injectCreateMessageEmbeddingService() {
        return this.createMessageEmbeddingService;
    }

    public GetBlobService injectGetBlobService() {
        return this.getBlobService;
    }

    public GetFormRecognizeResultService injectGetFormRecognizeResultService() {
        return this.getFormRecognizeResultService;
    }

    public DeleteBlobService injectDeleteBlobService() {
        return this.deleteBlobService;
    }
}
