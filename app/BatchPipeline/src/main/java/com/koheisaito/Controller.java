package com.koheisaito;

import java.util.List;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.TimerTrigger;

public class Controller {

    Factory factory = new Factory();

    /*
     * 1分おきに Blob Storage のファイルを読み込み、Azure Cache for Redis にテキスト埋め込みを保存する。
     * 埋め込み保存後、Blob Storage のファイルを削除する。
     */
    @FunctionName("ScheduleUpload")
    public void upload(
            @TimerTrigger(name = "scheduleUpload", schedule = "0 */1 * * * *") final String timerInfo,
            final ExecutionContext context) {
        List<String> blobUrls;
        try {
            blobUrls = factory.injectGetBlobService().getBlob();
        } catch (Exception e) {
            context.getLogger().warning(e.getMessage());
            return;
        }
        List<String> results = factory.injectGetFormRecognizeResultService().getFormRecognizeResult(blobUrls);
        factory.injectCreateMessageEmbeddingService().createMessageEmbedding(results);
        try {
            factory.injectDeleteBlobService().deleteBlobs(blobUrls);
        } catch (Exception e) {
            context.getLogger().warning(e.getMessage());
            return;
        }
    }
}
