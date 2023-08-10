# GifCreator

![GifCreator Logo](https://github.com/NeagDolph/GifCreator/blob/main/gifcreator_logo.png?raw=true)

A Spigot 1.19.4 GIF creation plugin utilizing the GIPHY API.
### [Download on Spigot](https://www.spigotmc.org/resources/gifcreator.111914/)

## What is GifCreator

GifCreator is a plugin you can use to create GIFs on maps in Minecraft. The plugin uses the [Giphy API](https://developers.giphy.com/) to retrieve GIFs using searches performed by users.

## Commands

* `/gif [search]` - Gets a GIF from GIPHY using the search term

## Permissions

* `gifcreator.gif` - Allows a user to run the `/gif` command

## Setup

> You will need a GIPHY API key. You can sign up for GIPHY and get one [here](https://developers.giphy.com/)

1. Download the JAR file.
2. Install the JAR file in the `plugins` folder of your server
3. Reload/Restart your server
4. Open the `config.yml` file in the `plugins/GifCreator` folder and put your GIPHY API key in the `giphyApiKey` field