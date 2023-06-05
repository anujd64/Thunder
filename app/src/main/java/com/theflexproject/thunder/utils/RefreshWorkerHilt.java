//package com.theflexproject.thunder.utils;
//
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//
//import androidx.annotation.NonNull;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//import com.theflexproject.thunder.database.AppDatabase;
//import com.theflexproject.thunder.database.DatabaseClient;
//import com.theflexproject.thunder.model.IndexLink;
//
//import java.util.List;
//
//import dagger.hilt.android.AndroidEntryPoint;
//
//
//        import android.content.Context;
//        import android.net.ConnectivityManager;
//        import android.net.NetworkInfo;
//
//        import androidx.annotation.NonNull;
//        import androidx.work.Worker;
//        import androidx.work.WorkerParameters;
//
//        import com.theflexproject.thunder.database.AppDatabase;
//        import com.theflexproject.thunder.database.DatabaseClient;
//        import com.theflexproject.thunder.model.IndexLink;
//
//        import java.util.List;
//
//        import dagger.hilt.android.AndroidEntryPoint;
//
//@AndroidEntryPoint
//public class RefreshWorkerHilt extends Worker {
//    Context ctxt ;
//    public RefreshWorker(
//            @NonNull Context mContext,
//            @NonNull WorkerParameters params)
//    {
//        super(mContext, params);
//        ctxt = mContext;
//    }
//
//    @Override
//    public Result doWork() {
//
//        // Do the work here--in this case, upload the images.
//
//        if(isNetworkAvailable()){
//
//            AppDatabase appDatabase = DatabaseClient.getInstance(ctxt).getAppDatabase();
//
//            SendGetRequestTMDB sendGetRequestTMDB = new SendGetRequestTMDB(ctxt,appDatabase);
//
//            SendPostRequest sendPostRequest = new SendPostRequest(
//                    ctxt,appDatabase,sendGetRequestTMDB
//            );
//            IndexUtils indexUtils = new IndexUtils(appDatabase,sendPostRequest);
//
//            List<IndexLink> indexLinkList = DatabaseClient.getInstance(ctxt).getAppDatabase().indexLinksDao().getAllEnabled();
//            for (IndexLink indexLink:indexLinkList) {
//                indexUtils.refreshIndex(indexLink);
//            }
//            return Result.success();
//        }
//        return Result.failure();
//        // Indicate whether the work finished successfully with the Result
//    }
//    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) ctxt.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
//}
