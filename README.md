### **⚡ Thunder** :

An Android app to stream and download your media stored in Google Drive in an Awesome way !!

(Just Movies for now)
<p align="center">
<a href="https://github.com/anujd64/Thunder/releases"><img src="https://img.shields.io/github/downloads/anujd64/Thunder/total?color=%233DDC84&logo=android&logoColor=%23fff&style=for-the-badge"></a>
</p>

*Haven't finalized the name yet I'm open to suggestions

### **🔘 Getting Started** :

Let's say you have a bunch of movies lying around in your Drive 
What you need to do is : 
1. Generate an index for the shared drive/folder
2. Deploy to Cloudflare (All instructions on the [Google Drive Index](https://gitlab.com/GoogleDriveIndex/Google-Drive-Index) its super easy)
3. Add the index link to movies folder link ( and username/password if you configured that for index ) in the settings tab, wait until it's done adding  

There you go you have your media Library which sources files from Drive.
(Probably will add video demo soon)

### **📱 Screenshots** :

<img src="https://github.com/anujd64/Thunder/blob/main/Screenshots/Home.jpg?raw=true" style="width: 23%;margin:16px;" />&nbsp;&nbsp;
<img src="https://github.com/anujd64/Thunder/blob/main/Screenshots/Library.jpg?raw=true" style="width: 23%;margin:16px;" />&nbsp;&nbsp;
<img src="https://github.com/anujd64/Thunder/blob/main/Screenshots/MovieDetails1.jpg?raw=true" style="width: 23%;margin:16px;" />&nbsp;&nbsp;
<img src="https://github.com/anujd64/Thunder/blob/main/Screenshots/MovieDetails2.jpg?raw=true" style="width: 23%;margin:16px;" />&nbsp;&nbsp;

### **🔥 Features** :

- Stream and Download media directly
- No Ads whatsoever
- Cool UI
- You can change Subtitle & Audio tracks while streaming 
- If your phone hardware supports it you can stream it whatever it maybe HDR, HDR10, Dolby Vision
- The app was made to solve a tiny problem with amazing projects like Plex and Jellyfin which cannot use Drive as a source directly



### **🛠️ To Do** :

- [ ] Add support for Shows
- [ ] Better Player UI
- [ ] Remember Playback Position 
- [ ] Add Sort to Library
- [ ] Browse by genre 
- [ ] Add animations
 

### **📋 Notes** :

- Added support GOIndex and Maple's GDindex (GOIndex is unreliable try refreshing if files aren't added)

- Currently video is only hardware decoded (I think) That's why HEVC content might or might not play at all

- Does not directly interact with Drive rather scrapes the index for movies. The index does the talking to the Drive api

- Sub-folders of current folder are searched now but if the folder has too many sub folders it will take longer to scan (This feature is not perfect yet but works fine for 100 or so folders)

- Naming of the files does matter it's better if your files are named like this
	```
	movie.2049.2160p.whatever
	movie.returns.2099.2160p.whatever

- Even if your files aren't correctly named there is an alternative algorithm which might just extract necessary info.

- Media files with no TMDB info are also shown in the library.

- Rarely some files might be mislabled if there is another movie of same name on TMDB.

- Ideally your folder should contain movie files only like

	``` 
	📂Movies
		└───movie.name.2049.2160p.garbage
		└───movie.returns.2069.2160p.whatever
		└───movie.returns.again.?.2099.2160p.whatever
		└───📂Subfolder <- this folder will scanned too
		
- If you want to request a feature create an issue with request feature tag
		

## Dependencies:
- Glide
- [FuzzyWuzzy](https://github.com/xdrop/fuzzywuzzy)
- Room DB
- Exoplayer
- BlurView

Special Thanks to:  
[Google Drive Index](https://gitlab.com/GoogleDriveIndex/Google-Drive-Index)  
[Bhadoo-Index-Scraper](https://github.com/sanjit-sinha/Bhadoo-Index-Scraper)

To build your Own:
Set the "TMDB_API_KEY" in Constants class to your key

### WANT TO CONTRIBUTE ?

All contributions are welcome also request new features and report any bugs or issue at discord/telegram.
[Discord](https://discord.gg/Y8h26bAm) & [Telegram](https://t.me/+qbLDmvEgC65lMWI1)

## DISCLAIMER

* Thunder only scrapes links from various drive indexes provided by user. 

* Thunder or its developer doesn't host any of the contents, it uses TMDB Api to fetch info. 


![Visitor count](https://shields-io-visitor-counter.herokuapp.com/badge?page=anujd64.Thunder)

