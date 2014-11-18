package davis.tileflip.helpers;

import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleApiHelper {
    private static GoogleApiClient apiClient;

    public static GoogleApiClient getApiClient() {
        return apiClient;
    }

    public static void setApiClient(GoogleApiClient client) {
        apiClient = client;
    }
}
