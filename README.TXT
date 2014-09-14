Simplified-Android-Networking
=============================

This application is an example I put together to use my networking library.
Unfortunately, my library is not a library, just another class in an android application. 
Development Goals: 
- Separation of Application and Library 
- Device-to-Device UDP/TCP Connections 
- Device-to-Server UDP/TCP Connections 
- Broadcast-to-Local Network 
- Abstract more code (eg; Users call my static class which calls my non-static code) 
- Error Checking (eg; Checks for connectivity) 
- Improved Comments, and Debug Messages 
- Using non-WiFi networks (eg; Cellular and bluetooth) 
- Learn correct lingo and stop using words as if I know what they mean, because I don't 
What Works: 
- Sending / Receiving UDP Broadcast messages 
- Sending / Receiving UDP messages within local network 	
  It is untested, but I don't see a technical reason why sending such a message
  across the internet wouldn't work, so long as port forwarding was set up properly. 
- Identifying device IP / Broadcast address 
- Maybe some other stuff

Limitations: 
Currently, I just started my last year of University Studies at Laurentian University.
I had been spending a lot of time developing this project during the Summer months,
but my workload will very likely prevent me from doing more than a few hours every few
weeks - or less. I don't know how to properly make this into a standalone library.
I would love to start reading one of the many tutorials available online, but given
my severe time constraints I'd much rather keep adding new features, by which I mean
making the features I've already added actually work.

About Why I am Making this Application: 
A friend of mine was writing an ambitious application that needed networking access to
function properly. He had already appealed to me and our other friends for help with the
basic programming tasks he had, but no one - myself included - answered his plea. I did
try to look into Android Studio, but it had overwhelmed me. A few months later, he made
another call, and this time I answered. He had too many important things to worry about
to try to figure out Android Networking, and I had just finished a class on Computer
Networks, although the course had nothing to do with Android. I decided to take on this
challenge. Starting was endlessly frustrating, just learning the interface was painful.
I do not use Eclipse to program Java, even though I've come to love Android Studio, I
still hate Eclipse (I can't compile with Eclipse, I don't get it what so ever.
I use jGrasp, because I prefer its minimalism). I was able to find a lot of code snippets
that were offered on StackOverflow. I try to address each author in my THANKS.TXT file,
but it can be very difficult when I am combining and modifying and searching, not to
mention that it isn't uncommon for the code I find to not be unique. I am absolutely
willing to remove any infringing content in my code immediately, and work to replace
the functionality later. I only hope that my code makes it easier for people to use
Android Networking. After I had a decent amount of work invested into this project, I was
told I should just use some generic Java library. I never even considered that before,
but, it's become a little hobby I enjoy.

How to Integrate my Code into your Project:
Download my AS project and copy NetworkingService.java to your application.
Your AndroidManifest.xml file needs to be modified to include which ever
permissions I have in my manifest - Currently INTERNET, ACCESS_WIFI_STATE,
and CHANGE_WIFI_MULTICATE_STATE are included.

This is using IntentService, so the tag
<service
  android:name=".NetworkingService" >
</service> 
needs to be included (inside the application tag, but above the activity tag).

VERY IMPORTANT: 
Look at MyActivity.java 
That is what I intend my static wrapper class to look like. It isn't anywhere near
ready, so you'll need to emulate it somehow. I won't talk about every method - I
tried to only have relevant methods - but the subclass ResponseReceiver is what
allows you to listen to the services. Each service (supposedly) runs on its own
thread, which is good for performance and responsiveness, but bad for me because
currently I can't address a particular service (Unless I forget to update this
text in the future, in which case I can totally respond to arbitrary services).

Change the package name, otherwise nothing will be found properly. 
Be sure to use Build > Clean Project, because that won't hurt.
I hope that's everything you need to know. Contact me, either on GitHub,
or at rx_paniw@laurentian.ca if you think I have infringing code. I might
take a few days to reply, but, I'll take it down then ask questions later.
If I don't believe you are being truthful, either intentionally or inadvertently,
I'll repost the code after I have given time to specifically investigate, or else I will
re-create the functionality of the code on my own or with other contributors.

If you think you know how to improve my work, please, tell me.
I started this as a beginner Android developer, though with a few years on Java.
Certainly there are things I am doing that happen to be wasting CPU cycles, or memory.


Again, you can email me for any questions or concerns at rx_pankiw@laurentian.ca
