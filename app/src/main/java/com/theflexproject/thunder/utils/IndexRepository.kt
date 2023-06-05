//package com.theflexproject.thunder.utils
//
//import android.os.Build
//import android.util.Base64
//import android.util.Log
//import com.google.gson.Gson
//import com.google.gson.stream.JsonReader
//import com.theflexproject.thunder.ApiUtils.GDIndexApi
//import com.theflexproject.thunder.ApiUtils.GoIndexApi
//import com.theflexproject.thunder.ApiUtils.TgIndexApi
//import com.theflexproject.thunder.Constants
//import com.theflexproject.thunder.MainActivity
//import com.theflexproject.thunder.database.DatabaseClient
//import com.theflexproject.thunder.model.File
//import com.theflexproject.thunder.model.Movie
//import com.theflexproject.thunder.model.ResFormat
//import com.theflexproject.thunder.model.SimpleLink
//import com.theflexproject.thunder.model.TVShowInfo.Episode
//import okhttp3.MediaType
//import okhttp3.RequestBody
//import org.jsoup.Jsoup
//import org.jsoup.nodes.Document
//import java.io.*
//import java.net.HttpURLConnection
//import java.net.URL
//import java.net.URLEncoder
//import java.nio.charset.StandardCharsets
//import java.util.*
//
//
//class IndexRepository {
//
//
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////object SendPostRequest {
//    private var nextPageToken = ""
//    private var pageIndex = 0
//
//    //    private static int retryLimit = 3;
//    fun postRequestGDIndex(
//        urlString: String,
//        user: String,
//        pass: String,
//        isTVShow: Boolean,
//        index_id: Int
//    ) {
//        var urlString = urlString
//        try {
//
//            //checkTrailing
//            if (urlString[urlString.length - 1] != '/') {
//                urlString += '/'
//            }
//
//            //createAuthValues
//            val user_pass = "$user:$pass"
//            val authHeaderValue: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                "Basic " + java.util.Base64.getEncoder().encodeToString(
//                    user_pass.toByteArray(
//                        StandardCharsets.UTF_8
//                    )
//                )
//            } else {
//                "Basic " + Arrays.toString(
//                    Base64.encode(
//                        user_pass.toByteArray(StandardCharsets.UTF_8),
//                        Base64.DEFAULT
//                    )
//                )
//            }
//
//            //makeTheApiCall
//            val call = GDIndexApi.getGDIndexService()
//                .getData(urlString, authHeaderValue, nextPageToken, pageIndex)
//            val response = call.execute()
//
//            //processTheResponseBody
//            if (response.isSuccessful && response.body() != null) {
//                val responseResult = StringBuilder(
//                    response.body()!!.string()
//                )
//                println("Undecrypted response$responseResult")
//                if (responseResult.isNotEmpty()
//                    &&
//                    responseResult.toString().contains("window.drive_names")
//                ) {
//                    var res = responseResult.substring(
//                        responseResult.indexOf("window.drive_names = JSON.parse('[") + 34,
//                        responseResult.indexOf("]")
//                    )
//                    res = res.replace("\"".toRegex(), "")
//                    val strings = res.split(",".toRegex()).dropLastWhile { it.isEmpty() }
//                        .toTypedArray()
//                    println(Arrays.toString(strings))
//                    for (i in strings.indices) {
//                        postRequestGDIndex("$urlString$i:/", user, pass, isTVShow, index_id)
//                    }
//                }
//
//                //Only for gd index as the response in encrypted
//                val reverseSb = responseResult.reverse()
//                val encodedString = reverseSb.substring(24, reverseSb.length - 20)
//                val decodedBytes: ByteArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    java.util.Base64.getDecoder().decode(encodedString)
//                } else {
//                    Base64.decode(encodedString, Base64.DEFAULT)
//                }
//                val decodedString = String(decodedBytes, StandardCharsets.UTF_8)
//                if (decodedString != "undefined") {
//                    println("Response from GD Index$decodedString")
//                    val gson = Gson()
//                    val target = gson.fromJson(decodedString, ResFormat::class.java)
//                    val folders: MutableList<String> = ArrayList()
//                    val files: List<File> = target.getData().getFiles()
//                    DatabaseClient.getInstance(MainActivity.context).appDatabase.resFormatDao()
//                        .insert(target)
//
//                    //send files to tmdb and insert them to db
//                    if (!isTVShow) {
//                        checkFilesAndSendToTMDBMovie(folders, files, urlString, index_id)
//                    } else {
//                        checkFilesAndSendToTMDBTVShow(folders, files, urlString, index_id)
//                    }
//                    if (target.nextPageToken != null) {
//                        nextPageToken = target.getNextPageToken()
//                        pageIndex++
//                        postRequestGDIndex(urlString, user, pass, isTVShow, index_id)
//                    }
//                    for (i in folders.indices) {
//                        pageIndex = 0
//                        nextPageToken = ""
//                        postRequestGDIndex(folders[i], user, pass, isTVShow, index_id)
//                        Log.i("Folder", folders[i])
//                    }
//                }
//            } else {
//                Log.i("PostRequestGDIndex", "Response is null")
//            }
//        } catch (e: Exception) {
//            Log.d("postRequestGDIndex", "Exception $e")
//        }
//    }
//
//    fun postRequestGoIndex(
//        urlString: String,
//        user: String,
//        pass: String,
//        isTVShow: Boolean,
//        index_id: Int
//    ) {
//        var urlString = urlString
//        try {
//            if (urlString[urlString.length - 1] != '/') {
//                urlString += '/'
//            }
//            val user_pass = "$user:$pass"
//            val user_pass_array = user_pass.toByteArray(StandardCharsets.UTF_8)
//            val authHeaderValue: String
//            authHeaderValue = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                "Basic " + java.util.Base64.getEncoder().encodeToString(user_pass_array)
//            } else {
//                "Basic " + Arrays.toString(
//                    Base64.encode(
//                        user_pass_array,
//                        Base64.DEFAULT
//                    )
//                ) // Unresolved reference: decode
//            }
//            val requestBody = RequestBody.create(
//                MediaType.parse("application/json"),
//                "{ \"q\":\"\",\"password\": null , \"page_index\": $pageIndex }"
//            )
//            val responseCall = GoIndexApi.getGoIndexService().getData(
//                urlString,
//                requestBody,
//                "application/json",
//                "application/json",
//                authHeaderValue
//            )
//            val response = responseCall.execute()
//            if (response.isSuccessful && response.body() != null) {
//                val responseResult = StringBuilder(
//                    response.body()!!.string()
//                )
//                println("GoIndex Response$responseResult")
//                val gson = Gson()
//                val target = gson.fromJson(
//                    responseResult.toString(),
//                    ResFormat::class.java
//                )
//                val folders: MutableList<String> = ArrayList()
//                val files: List<File> = target.getData().getFiles()
//                DatabaseClient.getInstance(MainActivity.context).appDatabase.resFormatDao()
//                    .insert(target)
//                if (!isTVShow) {
//                    checkFilesAndSendToTMDBMovie(folders, files, urlString, index_id)
//                } else {
//                    checkFilesAndSendToTMDBTVShow(folders, files, urlString, index_id)
//                }
//                if (target.nextPageToken != null) {
//                    nextPageToken = target.nextPageToken
//                    pageIndex++
//                    postRequestGoIndex(urlString, user, pass, isTVShow, index_id)
//                }
//                for (i in folders.indices) {
//                    pageIndex = 0
//                    nextPageToken = ""
//                    postRequestGoIndex(folders[i], user, pass, isTVShow, index_id)
//                    Log.i("Folder", folders[i])
//                }
//            }
//        } catch (e: IOException) {
//            println("Exception in Goindex")
//        }
//    }
//
//    fun postRequestMapleIndex(
//        urlString: String,
//        user: String,
//        pass: String,
//        isTVShow: Boolean,
//        index_id: Int
//    ) {
//        var urlString = urlString
//        try {
//            if (urlString[urlString.length - 1] != '/') {
//                urlString += '/'
//            }
//            urlString += "?rootId=root"
//            val url = URL(urlString)
//            val user_pass = "$user:$pass"
//            val user_pass_array = user_pass.toByteArray(StandardCharsets.UTF_8)
//            val authHeaderValue: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                "Basic " + java.util.Base64.getEncoder().encodeToString(user_pass_array)
//            } else {
//                "Basic " + Arrays.toString(
//                    Base64.encode(
//                        user_pass_array,
//                        Base64.DEFAULT
//                    )
//                ) // Unresolved reference: decode
//            }
//            //        String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(user_pass_array);
//            println(authHeaderValue)
//            var conn = url.openConnection() as HttpURLConnection
//            var br: BufferedReader? = null
//            var failed = false
//            try {
//                conn.requestMethod = "POST"
//                conn.setRequestProperty("Content-Type", "application/json")
//                conn.setRequestProperty("Accept", "application/json")
//                conn.setRequestProperty("Authorization", authHeaderValue)
//                conn.doOutput = true
//                br = BufferedReader(InputStreamReader(conn.inputStream, StandardCharsets.UTF_8))
//            } catch (e: FileNotFoundException) {
//                failed = true
//                println("Failed in the initial attempt $e")
//            }
//            val code = conn.responseCode
//            println("HTTP CODE$code")
//            var tryCount = 0
//            while ((code != 200 || failed) && tryCount < 5) {
//                println("Request is not successful - $tryCount")
//                tryCount++
//                //            conn.getInputStream().close();
//                conn.disconnect()
//                conn = url.openConnection() as HttpURLConnection
//                conn.requestMethod = "POST"
//                conn.setRequestProperty("Content-Type", "application/json")
//                conn.setRequestProperty("Accept", "application/json")
//                conn.setRequestProperty("Authorization", authHeaderValue)
//                conn.doOutput = true
//                try {
//                    br = BufferedReader(InputStreamReader(conn.inputStream, StandardCharsets.UTF_8))
//                } catch (e: FileNotFoundException) {
//                    failed = true
//                    println("Failed in the retry attempt $e")
//                }
//            }
//            if (br != null) {
//                val sb = StringBuilder()
//                var c: Int
//                while (br.read().also { c = it } >= 0) {
//                    sb.append(c.toChar())
//                }
//                br.close()
//                println("Response from Maple Index$sb")
//                val gson = Gson()
//                val target = gson.fromJson(sb.toString(), ResFormat::class.java)
//                val folders: MutableList<String> = ArrayList()
//                val files: List<File> = target.getData().getFiles()
//
////                if (files == null) {
////
////                    while (retryLimit >= 0) {
////                        postRequestMapleIndex(urlString , user , pass , isTVShow , index_id);
////                        retryLimit--;
////                    }
////                }
//                DatabaseClient.getInstance(MainActivity.context).appDatabase.resFormatDao()
//                    .insert(target)
//                if (!isTVShow) {
//                    checkFilesAndSendToTMDBMovie(folders, files, urlString, index_id)
//                } else {
//                    checkFilesAndSendToTMDBTVShow(folders, files, urlString, index_id)
//                }
//                if (target.nextPageToken != null) {
//                    nextPageToken = target.getNextPageToken()
//                    pageIndex++
//                    postRequestMapleIndex(urlString, user, pass, isTVShow, index_id)
//                }
//                for (i in folders.indices) {
//                    pageIndex = 0
//                    nextPageToken = ""
//                    postRequestMapleIndex(folders[i], user, pass, isTVShow, index_id)
//                    Log.i("Folder", folders[i])
//                }
//            }
//        } catch (e: IOException) {
//            println("Exception in maple")
//        }
//    }
//
//    fun postRequestSimpleProgramIndex(
//        urlString: String,
//        user: String?,
//        pass: String?,
//        isTVShow: Boolean,
//        index_id: Int
//    ) {
//        var urlString = urlString
//        try {
//            if (urlString[urlString.length - 1] != '/') {
//                urlString += '/'
//            }
//            val params: MutableMap<String, Any> = LinkedHashMap()
//            params["page_token"] = nextPageToken
//            params["page_index"] = pageIndex
//            params["password"] = ""
//            val postData = StringBuilder()
//            for ((key, value) in params) {
//                if (postData.isNotEmpty()) postData.append('&')
//                postData.append(URLEncoder.encode(key, "UTF-8"))
//                postData.append('=')
//                postData.append(URLEncoder.encode(value.toString(), "UTF-8"))
//            }
//            val s = "?$postData"
//            println("inside simple$urlString")
//            val url = URL(urlString + s)
//            println("Url built with params$postData")
//            var conn = url.openConnection() as HttpURLConnection
//            var br: BufferedReader? = null
//            var failed = false
//            try {
//                conn.requestMethod = "GET"
//                conn.setRequestProperty("cf_cache_token", Constants.CF_CACHE_TOKEN)
//                br = BufferedReader(InputStreamReader(conn.inputStream, StandardCharsets.UTF_8))
//            } catch (e: FileNotFoundException) {
//                failed = true
//                println("Failed in the initial attempt $e")
//            }
//            val code = conn.responseCode
//            println("HTTP CODE$code")
//            var tryCount = 0
//            while ((code != 200 || failed) && tryCount < 5) {
//                println("Request is not successful - $tryCount")
//                tryCount++
//                //            conn.getInputStream().close();
//                conn.disconnect()
//                conn = url.openConnection() as HttpURLConnection
//                conn.requestMethod = "GET"
//                conn.setRequestProperty(
//                    "cf_cache_token",
//                    "UKsVpQqBMxB56gBfhYKbfCVkRIXMh42pk6G4DdkXXoVh7j4BjV"
//                )
//                try {
//                    br = BufferedReader(InputStreamReader(conn.inputStream, StandardCharsets.UTF_8))
//                } catch (e: FileNotFoundException) {
//                    failed = true
//                    println("Failed in the retry attempt $e")
//                }
//            }
//            if (br != null) {
//                val sb = StringBuilder()
//                var c: Int
//                while (br.read().also { c = it } >= 0) {
//                    sb.append(c.toChar())
//                }
//                br.close()
//                println("Response from GD Index$sb")
//                val gson = Gson()
//                val target = gson.fromJson(sb.toString(), ResFormat::class.java)
//                val folders: MutableList<String> = ArrayList()
//                val files: List<File> = target.getData().getFiles()
//                DatabaseClient.getInstance(MainActivity.context).appDatabase.resFormatDao()
//                    .insert(target)
//
//
//                //send files to tmdb and insert them to db
//                if (!isTVShow) {
//                    checkFilesAndSendToTMDBMovie(folders, files, urlString, index_id)
//                } else {
//                    checkFilesAndSendToTMDBTVShow(folders, files, urlString, index_id)
//                }
//                if (target.nextPageToken != null) {
//                    nextPageToken = target.getNextPageToken()
//                    pageIndex++
//                    postRequestSimpleProgramIndex(urlString, user, pass, isTVShow, index_id)
//                }
//                //                if (files == null) {
////                    while (retryLimit >= 0) {
////                        System.out.println("likely a worker exception occurred");
////                        postRequestSimpleProgramIndex(urlString , user , pass , isTVShow , index_id);
////                        retryLimit--;
////                    }
////                }
//                for (i in folders.indices) {
//                    pageIndex = 0
//                    nextPageToken = ""
//                    postRequestSimpleProgramIndex(
//                        folders[i], user, pass, isTVShow, index_id
//                    )
//                    Log.i("Folder", folders[i])
//                }
//            }
//        } catch (e: IOException) {
//            println("Exception in simple")
//        }
//    }
//
//    private fun generateDownloadLinkSimpleProgram(id: String): String? {
//        try {
//            val url = URL(Constants.SIMPLE_PROGRAM_DOWNLOAD_API)
//            val conn = url.openConnection() as HttpURLConnection
//            conn.requestMethod = "GET"
//            val code = conn.responseCode
//            println("HTTP CODE$code")
//            val br = BufferedReader(InputStreamReader(conn.inputStream, StandardCharsets.UTF_8))
//            val sb = StringBuilder()
//            var c: Int
//            while (br.read().also { c = it } >= 0) {
//                sb.append(c.toChar())
//            }
//            br.close()
//            println("Response from GD Index$sb")
//            val a = "{" + sb.toString().replace(";".toRegex(), ",").replace("=".toRegex(), ":")
//                .replace("const ".toRegex(), "").replace("var ".toRegex(), "") + "}"
//            val gson = Gson()
//            val target = gson.fromJson(a, SimpleLink::class.java)
//            return "http://api." + target.getArrayofworkers()[0] + ".workers.dev/download/" + id
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return null
//    }
//
//    fun getRequestTgIndex(urlString: String, isTVShow: Boolean, index_id: Int) {
//        val doc: Document
//        try {
//            val call = TgIndexApi.getTgIndexService().getResponseBody(urlString)
//            val response = call.execute()
//            if (response != null && response.isSuccessful && response.body() != null) {
//                val body = response.body()!!.string()
//                println("Response from TG Index$body")
//                doc = Jsoup.parse(body)
//                val parsedUrl = URL(urlString)
//                val base = parsedUrl.protocol + "://" + parsedUrl.host
//                val files: MutableList<File> = ArrayList()
//                getFiles(files, base, doc)
//                if (!isTVShow) {
//                    checkFilesAndSendToTMDBMovie(null, files, urlString, index_id)
//                } else {
//                    checkFilesAndSendToTMDBTVShow(null, files, urlString, index_id)
//                }
//            }
//            //            String substring = urlString.substring(0, urlString.length() - 3);
////            System.out.println(substring);
////            Connection.Response res = Jsoup.connect(substring + "login")
////                    .data("remember", "true", "redirect_to", "", "username", user, "password", pass)
////                    .method(Connection.Method.POST)
////                    .execute();
////
////            Map<String, String> loginCookies = res.cookies();
//
////            doc = Jsoup.connect(urlString)
////                    .cookies(loginCookies)
////                    .get();
//
////                Connection.Response loginForm = Jsoup.connect(urlString)
////                        .method(Connection.Method.GET)
////                        .data("remember","true")
////                        .data("redirect_to", "")
////                        .data("username", user)
////                        .data("password", pass)
////                        .execute();
////
////                doc = Jsoup.connect(urlString)
////                        .cookies(loginForm.cookies())
////                        .post();
////            System.out.println(doc);
//
//
////                Map<String, String> params = new LinkedHashMap<>();
////                params.put("username" , user);
////                params.put("password" , pass);
////                params.put("remember","true") ;
////                params.put("redirect_to", "");
////
////                doc = Jsoup.connect(urlString)
////                        .data(params)
////                        .get();
////
////                System.out.println(doc.text());
////                    HashMap<String, String> formData = new HashMap<>();
////                formData.put("username", user);
////                formData.put("password", pass);
//
////                Connection.Response loginForm = Jsoup.connect(urlString)
////                        .method(Connection.Method.POST)
////                        .data(formData)
////                        .execute();
////                doc = Jsoup.connect(urlString)
////                        .data(formData)
////                        .get();
////            doc = Jsoup.connect(urlString).data("username",user).data("password",pass).get();
//        } catch (e: IOException) {
//            throw RuntimeException(e)
//        }
//    }
//
//    private fun getFiles(files: MutableList<File>, base: String, doc: Document) {
//        val cards =
//            doc.getElementsByClass("text-sm items-center justify-center w-full min-h-full sm:w-2/5 md:w-1/4 lg:w-1/6 rounded m-2 shadow hover:shadow-lg dark:bg-red-700")
//        println("tgindex response $cards")
//        var i = 0
//        for (card in cards) {
//            if (card.attr("title").contains("video/")) {
//                val name = """
//                    ${
//                    card.getElementsByClass("p-2")[0].text().replace("\\*".toRegex(), "")
//                        .replace("@".toRegex(), "")
//                }
//
//                    """.trimIndent()
//                val urlString = base + "/" + card.select("a[href]")[1].attr("href")
//                println("$i\t Final Url inside tgindex getfiles$urlString\n")
//                println(
//                    "$i $name$urlString"
//                )
//                val file = File()
//                file.setName(name.replace("/*".toRegex(), ""))
//                file.setUrlString(urlString)
//                file.setMimeType("video/x-matroska")
//                files.add(file)
//                i++
//            }
//        }
//        /** for now does not work  */
//
////        Elements getNextPageTitle = doc.select("a.mx-2");
////        Element nextLink = getNextPageTitle.last();
////        if (nextLink != null && !getNextPageTitle.isEmpty()) {
////            String nextPageUrl = nextLink.absUrl("href");
//////        Element getNextPageTitle = doc.getElementsByClass("mx-2 p-2 border rounded bg-green-500 hover:border-green-500 hover:text-green-500 hover:bg-white").first();
//////        if (getNextPageTitle !=null && !getNextPageTitle.absUrl("href").isEmpty()) {
////            try {
////                String urlToNextPage = base + "/" + nextPageUrl;
////
////                System.out.println("Next Page Url " + urlToNextPage + "\n");
////                Call<ResponseBody> call = TgIndexApi.getTgIndexService().getResponseBody(urlToNextPage);
////                Response<ResponseBody> response = call.execute();
////
////                if (response != null && response.isSuccessful() && response.body() != null) {
////                    String body = response.body().string();
////                    System.out.println("Response from TG Index" + body);
////                    doc = Jsoup.parse(body);
//////                        files.addAll(getFiles(Jsoup.connect(mTitle.absUrl("href")).get(), user, pass));
//////                    files.addAll(
////                            getFiles(files,base, doc, user, pass);
//////                    );
////                }
////            } catch (IOException e) {
////                throw new RuntimeException(e);
////            }
////        }
////        }
////        return files;
//    }
//
//    private fun checkFilesAndSendToTMDBMovie(
//        folders: MutableList<String>?,
//        files: List<File>?,
//        urlString: String,
//        index_id: Int
//    ) {
//        val gson = Gson()
//        if (files != null) {
//            try {
//                println("Files from the index$files")
//                for (i in files.indices) {
//                    val file = files[i]
//                    if (file.getMimeType() == "video/x-matroska" || file.getMimeType() == "video/mp4" || file.getMimeType() == "video/x-msvideo" || file.getMimeType() == "video/mpeg" || file.getMimeType() == "video/webm") {
//                        val test = isAlreadyPresent(file.getId(), file.getModifiedTime())
//                        if (!test) {
//                            println("the file is not present")
//                            val callingMethodName = Thread.currentThread().stackTrace[3].methodName
//                            println("Calling method name $callingMethodName")
//                            if (callingMethodName == "postRequestSimpleProgramIndex") {
//                                file.urlstring = generateDownloadLinkSimpleProgram(file.getId())
//                            } else if (file.getUrlString() == null) {
//                                file.urlstring = urlString + file.getName()
//                            }
//                            println("File to string$file")
//                            try {
//                                val reader = JsonReader(StringReader(file.toString()))
//                                reader.isLenient = true
//                                val movie = gson.fromJson<Movie>(
//                                    reader,
//                                    Movie::class.java
//                                )
//
////                            Movie movie = gson.fromJson(file.toString() , Movie.class);
//                                movie.setFileName(file.getName())
//                                movie.setGd_id(file.getId())
//                                movie.setIndex_id(index_id)
//
////                            long modifiedTimeLong = dateToLong(file.getModifiedTime());
//                                movie.setModifiedTime(file.getModifiedTime())
//                                //                            movie.setModifiedTime(modifiedTimeLong);
//                                SendGetRequestTMDB.sendGet2(movie) //tmdbrequest
//                                Log.i("Movie", movie.toString())
//                            } catch (e: Exception) {
//                                println("Malformed Json in checkFilesAndSendToTMDBMovie$e")
//                            }
//                        }
//                    } else if (file.getMimeType() == "application/vnd.google-apps.folder" && folders != null) {
//                        folders.add(urlString + file.getName() + "/")
//                    }
//                }
//            } catch (e: NullPointerException) {
//                Log.i("Exception null pointer", e.toString())
//            }
//        }
//    }
//
//    private fun isAlreadyPresent(id: String, modifiedTime: Date): Boolean {
//        println("id to test $id")
//        val movie =
//            DatabaseClient.getInstance(MainActivity.context).appDatabase.movieDao().getByGdId(id)
//        val episode =
//            DatabaseClient.getInstance(MainActivity.context).appDatabase.episodeDao().findByGdId(id)
//        if (movie != null && modifiedTime.after(movie.getModifiedTime())) {
//            DatabaseClient.getInstance(MainActivity.context).appDatabase.movieDao().deleteByGdId(id)
//            return false
//        }
//        if (episode != null && modifiedTime.after(episode.getModifiedTime())) {
//            DatabaseClient.getInstance(MainActivity.context).appDatabase.episodeDao()
//                .deleteByGdId(id)
//            return false
//        }
//        return if (movie == null && episode == null) {
//            println("not present in movies")
//            false
//        } else {
//            true
//        }
//    }
//
//    private fun checkFilesAndSendToTMDBTVShow(
//        showFolders: MutableList<String>?,
//        files: List<File>?,
//        urlString: String,
//        index_id: Int
//    ) {
//        val gson = Gson()
//        if (files != null) {
//            try {
//                for (i in files.indices) {
//                    val file = files[i]
//                    if (file.getMimeType() == "video/x-matroska" || file.getMimeType() == "video/mp4" || file.getMimeType() == "video/x-msvideo" || file.getMimeType() == "video/mpeg" || file.getMimeType() == "video/webm") {
//                        val test = isAlreadyPresent(file.getId(), file.getModifiedTime())
//                        if (!test) {
//                            val callingMethodName = Thread.currentThread().stackTrace[3].methodName
//                            if (callingMethodName == "postRequestSimpleProgramIndex") {
//                                file.urlstring = generateDownloadLinkSimpleProgram(file.getId())
//                            } else {
//                                file.urlstring = urlString + file.getName()
//                            }
//                            val episode = gson.fromJson(file.toString(), Episode::class.java)
//                            episode.setFileName(file.getName())
//                            episode.setModifiedTime(file.getModifiedTime())
//                            episode.setGd_id(file.getId())
//                            episode.setIndex_id(index_id)
//                            println("episode before tmdb$episode")
//                            SendGetRequestTMDB.sendGetTVShow(episode) //tmdbrequest
//                        }
//                    } else if (file.getMimeType() == "application/vnd.google-apps.folder" && showFolders != null) {
//                        showFolders.add(urlString + file.getName() + "/")
//                        Log.i("showFolders", showFolders.toString() + file.getName())
//                    }
//                }
//            } catch (e: NumberFormatException) {
//                Log.i("Exception", e.toString())
//            }
//        }
//    }
//}