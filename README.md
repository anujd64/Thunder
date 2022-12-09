### **⚡ Thunder** :

An Android app to stream and download your media stored in Google Drive in an Awesome way !!

<p align="center">
<a href="https://github.com/anujd64/Thunder/releases"><img src="https://img.shields.io/github/downloads/anujd64/Thunder/total?color=%233DDC84&logo=android&logoColor=%23fff&style=for-the-badge"></a>
</p>

If you feel like supporting me :

<a href="https://www.buymeacoffee.com/anujd"><img src="https://img.buymeacoffee.com/button-api/?text=Buy me a coffee&emoji=&slug=anujd&button_colour=FF5F5F&font_colour=ffffff&font_family=Cookie&outline_colour=000000&coffee_colour=FFDD00" /></a>

### **🔘 Getting Started** :

Let's say you have a bunch of movies or tv shows lying around in your Drive 
What you need to do is : 
1. Generate an index for the shared drive/folder
2. Deploy to Cloudflare (All instructions on the [Google Drive Index](https://gitlab.com/GoogleDriveIndex/Google-Drive-Index) its super easy)
3. Add the index link to movies folder link ( and username/password if you configured that for index ) in the settings tab, wait until it's done adding  

There you go you have your media Library which sources movies from Drive.
(Probably will add video demo soon)

### **📱 Screenshots** :

<img src="https://github.com/anujd64/Thunder/blob/main/Screenshots/Home.png?raw=true" style="width: 23%;margin:16px;" />&nbsp;&nbsp;
<img src="https://github.com/anujd64/Thunder/blob/main/Screenshots/Home2.png?raw=true" style="width: 23%;margin:16px;" />&nbsp;&nbsp;
<img src="https://github.com/anujd64/Thunder/blob/main/Screenshots/Library-Movies.png?raw=true" style="width: 23%;margin:16px;" />&nbsp;&nbsp;
<img src="https://github.com/anujd64/Thunder/blob/main/Screenshots/Library-TV.png?raw=true" style="width: 23%;margin:16px;" />&nbsp;&nbsp;
<img src="https://github.com/anujd64/Thunder/blob/main/Screenshots/MovieDetails.png?raw=true" style="width: 23%;margin:16px;" />&nbsp;&nbsp;
<img src="https://github.com/anujd64/Thunder/blob/main/Screenshots/TVDetails.png?raw=true" style="width: 23%;margin:16px;" />&nbsp;&nbsp;
<img src="https://github.com/anujd64/Thunder/blob/main/Screenshots/TVDetailsBottom.jpg?raw=true" style="width: 23%;margin:16px;" />&nbsp;&nbsp;
<img src="https://github.com/anujd64/Thunder/blob/main/Screenshots/TVSeasonDetails.jpg?raw=true" style="width: 23%;margin:16px;" />&nbsp;&nbsp;
<img src="https://github.com/anujd64/Thunder/blob/main/Screenshots/TVEpisodeList.jpg?raw=true" style="width: 23%;margin:16px;" />&nbsp;&nbsp;
<img src="https://github.com/anujd64/Thunder/blob/main/Screenshots/TVEpisodeDetails.png?raw=true" style="width: 23%;margin:16px;" />&nbsp;&nbsp;


### **🔥 Features** :

- Stream and Download media directly
- Now Supports adding tv show folders too!!
- Supports Google Drive Index (Recommended) , GO Index , MapleIndex
- No Ads whatsoever
- Cool UI
- You can change Subtitle & Audio tracks while streaming 
- If your phone hardware supports it you can stream it whatever it maybe HDR, HDR10, Dolby Vision
- The app was made to solve a tiny problem with amazing projects like Plex and Jellyfin which cannot use Drive as a source directly



### **🛠️ To Do** :

- [x] Add support for Shows
- [x] Add animations
- [ ] Better Player UI
- [ ] Remember Playback Position 
- [ ] Add Sort to Library
- [ ] Browse by genre 


### **📋 Notes** :

- Added support GOIndex and Maple's GDindex (GOIndex is unreliable try refreshing if movies aren't added)

- Currently video is only hardware decoded (I think) That's why HEVC content may or may not play at all

- Does not directly interact with Drive rather scrapes the index for movies. The index does the talking to the Drive api

- Sub-folders of current folder are searched now but if the folder has too many sub folders it will take longer to scan

- Even if your movies aren't correctly named there is an alternative algorithm which might just extract necessary info

- Media movies with no TMDB info are also shown in the library

- Rarely some movies may be mislabled if there is another movie of same name on TMDB

- Naming of the movies does matter it's better if your movies are named like this
	```
	movie.2049.2160p.whatever
	movie.returns.2099.2160p.whatever
- TV show are recognized based on the naming of an episode file so every episode must be named like this (probably will work if naming follows Scene naming conventions):
	```
	Show Name S01 E01
	Show.Name.S01.E01.2160p.whatever

- Ideally your folder should contain movie movies only like

	``` 
	📂Movies
		└───movie.name.2049.2160p.garbage
		└───movie.returns.2069.2160p.whatever
		└───movie.returns.again.?.2099.2160p.whatever
		└───📂Subfolder <- this folder will be scanned too
		
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

###To build your Own:

Set the "TMDB_API_KEY" and put one or more FanArt API keys in the KEYS array inside the Constants class.

Here's a guide to build your own apk using github by [dishapatel1010](https://github.com/dishapatel010) : https://github.com/dishapatel010/Thunder/tree/Build

### WANT TO CONTRIBUTE ?

All contributions are welcome also request new features and report any bugs or issue at discord/telegram.
[Discord](https://discord.gg/Y8h26bAm) & [Telegram](https://t.me/+qbLDmvEgC65lMWI1)

## DISCLAIMER

* Thunder only scrapes links from various drive indexes provided by user. 

* Thunder or its developer doesn't host any of the contents, it uses TMDB Api to fetch info. 


![Visitor count](https://shields-io-visitor-counter.herokuapp.com/badge?page=anujd64.Thunder)

