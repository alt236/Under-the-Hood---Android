Under the Hood
========================
Description
-----------
The application will run a number of shell commands to get info from the device.

Even though root not needed, rooted devices will give more info.

As the available commands change depending on the ROM used, some commands will fail to work.

Results can be exported to the SD card or shared using the standard android share dialogue (so that they can be e-mailed, for example).

Notes
----------
* This is a personal debug tool, but it might be of use to someone else.
* If the device is rooted, then all commands will be executed as root. If not, then some commands will not return any data.
* I am certain that not all ROMs (factory, or custom) support all commands, but any errors should handled gracefully.

Changelog
-----------
* v0.0.1: First public release.
* v0.0.2: Font size change buttons in menu.
* v0.0.3: Added "ip route show" and "ip -f inet6 route show", font size increase.
*v0.0.4: Threaded the command execution, added execution choice dialogue.
* v0.0.5: Fixed "Other" tab, changed required Android version to 1.5 (it was 2.1), added a bit more information.
* v0.0.6: Changed required version to 1.6 as 1.5 broke large screens, changed icon, added more information.
* v0.0.7: Added parsing of /proc, rearranged tab contents.
* v0.0.8: Added more commands, added group separators.

Permission Explanation
-----------
* No permissions needed.

Build Instructions
--------------
You will need the following libraries to build this project:
* ActionbarSherlock v4+: [https://github.com/JakeWharton/ActionBarSherlock]()

Acknowledgments
-----------
All logos are the property of their respective owners

License
-----------
Licensed under the Apache License 2.0: [http://www.apache.org/licenses/LICENSE-2.0.txt]()
	
Links
-----------
* Market link: [https://market.android.com/details?id=aws.apps.androidDrawables]()
* Webpage: [http://aschillings.co.uk/html/android_drawables.html]()
* Github: [https://github.com/alt236/Android-Drawables---Android]()