The implementation of inviting the musician:

In the code I try to invite the minimum number of musicians needed for playing the three compositions.
I first find out the number of different types of musician needed in every composition and find out the minimum number of 
different type of musicians needed for playing those three compositions.
Every time when playing the composition the musicians registered in the conductor will be cleared at first
And then registering again as required from the invited musicians.
After playing those three compositions, each invited musicians may have a chance of 50% of leaving the band
The leaving musicians will be saved in an arraylist and displayed
The staying musicians will remain in the invited list for the performance next year
When there are rooms for extra musicians to join the band they will be added in the band.

The implementation of the extension part(making arbitrary note length different):

In reading the length of notes I have made another line for reading the length of different notes
The length of notes are corresponding to the respective positions of the notes in the file
The length of notes avalible are whole note, half note, quarter note, Eighth note, Sixteenth note, and Thirty-second note
they are in the format of (1,2,4,8,16,32) 
In the document they are embraced by a pair of {} such as {8,8,8,8,4,4,4}
A new method call addSpeed(String [] speed) is used to add the length of notes
while speed[] is the array storing the different length of notes 
The array of length of notes will be translated to duration (ms) of playing the note.
The duration depends on the tempo of the song.
In the original version of the conductor class it controls the length of notes by Thread.sleep(getNoteLength()) with fixed value of which 
depends on the tempo. 
Now the function getNoteLength() has been changed to take one argument to getNoteLength(int index), in which index represents the note being played
at the moment. The function will then return the note length of that index.
This makes the program capable of playing music with different note length
However it cannot play notes of which have different length in different instrument.