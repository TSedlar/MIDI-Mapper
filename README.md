# MIDI-Mapper
[![](https://img.shields.io/github/license/mashape/apistatus.svg)](LICENSE)
[![](https://img.shields.io/badge/donate-patreon-orange.svg)](https://www.patreon.com/bePatron?c=954360)
[![](https://img.shields.io/badge/donate-paypal-blue.svg)](https://paypal.me/TSedlar)

### A program used to map MIDI buttons to computer actions

## Requirements
- Java 8

## Installing
The latest jar can be downloaded [here](https://github.com/TSedlar/MIDI-Mapper/releases)

## Screenshots

![](wiki/screenshot.png)

## OBS

This can be used with OBS as an alternative to a Stream Deck.

It requires [obs-websocket](https://github.com/Palakis/obs-websocket) to be installed.

### Supported Operations
![](wiki/obs-1.png)

### Example Integrations
![](wiki/obs-2.png)

Arguments should be separated with a comma.

As can be seen in the scene changes, if a transition is wanted, the name of the transition should be included after the name of the scene.

If using a knob to change volume of a source, no argument is needed.

If using a button to change volume, one needs to include it after the source name: `Yeti, 0.5` (change yeti to 50% volume)

### Example devices

![](wiki/launchpad-mini.jpg)
![](wiki/launchkey-mini.jpg)
