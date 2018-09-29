grammar ImmerseAdventureModule;

action: playAudioAction | playBackgroundMusicAction | stopBackgroundMusicAction;

playAudioAction: resourceSection volumeSection? (sourceSpeakerSection | sourceLocationSection)? listenerLocationSection? normalizeSection? playbackSection?;

resourceSection: fileResource | urlResource;

fileResource: 'play ' filename=FILENAME;

urlResource: 'play ' urlString=URL (format=' with format (' (stereo='S-'|mono='M-') sampleRate=INTEGER 'K-' (oneByte='1B-'|twoBytes='2B-') (signed='s-'|unsigned='u-') (littleEndian='l)'|bigEndian='b)') )?;

volumeSection: ' at volume ' volume=INTEGER;

sourceSpeakerSection: singleSpeaker | multipleSpeakers | allSpeakers;

singleSpeaker: ' at speaker ' speakerId=INTEGER;

multipleSpeakers: ' at speakers ' speakerIds=(INTEGER|INTEGER_LIST);

allSpeakers: ' at all speakers';

sourceLocationSection: locationSection;

listenerLocationSection: ' with listener' locationSection;

locationSection: fixedLocation | pathLocation | circlingLocation;

fixedLocation: ' at location ' location=VECTOR_3D;

pathLocation: ' moving on path ' path=(VECTOR_3D|PATH) (' with speed ' speed=(INTEGER|DOUBLE))?;

circlingLocation: ' circling' (clockwise=' clockwise' | antiClockwise=' anti-clockwise')? ' around ' center=VECTOR_3D ' with radius ' radius=(INTEGER|DOUBLE)
                  (' starting at angle ' startAngle=(INTEGER|DOUBLE))? ' with speed ' speed=(INTEGER|DOUBLE);

normalizeSection: asOneSpeaker=' as one speaker' | asAllSpeakers=' as all speakers';

// TODO: time should be more flexible
playbackSection: once=' once' | ' repeat ' repeat=INTEGER ' times' | forever=' repeat forever' | ' for ' seconds=INTEGER ' seconds';

playBackgroundMusicAction: backgroundResourceSection volumeSection?;

backgroundResourceSection: 'play background music ' filename=FILENAME;

stopBackgroundMusicAction: 'stop background music';

// TODO: Use fragments for number, word, etc

INTEGER: [0-9]+;

INTEGER_LIST: [0-9]+ (',' [0-9]+)*;

DOUBLE: [0-9]+ '.' [0-9]+;

FILENAME: [A-Za-z] [A-Za-z0-9\\.]*;

URL: 'http' 's'? '://' [^ ]+;

VECTOR_3D: '(' [0-9]+ ',' [0-9]+ ',' [0-9]+ ')';

PATH: '(' [0-9]+ ',' [0-9]+ ',' [0-9]+ ')' (';' '(' [0-9]+ ',' [0-9]+ ',' [0-9]+ ')')*;
