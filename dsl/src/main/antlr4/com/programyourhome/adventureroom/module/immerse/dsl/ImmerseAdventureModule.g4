grammar ImmerseAdventureModule;

action: playAudioAction | playBackgroundMusicAction | stopBackgroundMusicAction;

playAudioAction: resourceSection volumeSection? (sourceSpeakerSection | sourceLocationSection)? listenerLocationSection?;

resourceSection: 'play ' filename=FILENAME;

volumeSection: ' at volume ' volume=INTEGER;

sourceSpeakerSection: singleSpeaker | multipleSpeakers | allSpeakers;

singleSpeaker: ' at speaker ' speakerId=INTEGER;

multipleSpeakers: ' at speakers ' speakerIds=INTEGER_LIST;

allSpeakers: ' at all speakers';

sourceLocationSection: locationSection;

listenerLocationSection: locationSection;

locationSection: fixedLocation | pathLocation | circlingLocation;

fixedLocation: ' at location ' location=VECTOR_3D;

pathLocation: ' moving on path ' path=PATH (' at speed ' speed=INTEGER)?;

circlingLocation: 'none';

playBackgroundMusicAction: backgroundResourceSection volumeSection?;

backgroundResourceSection: 'play background music ' filename=FILENAME;

stopBackgroundMusicAction: 'stop background music';

// TODO: Use fragments for number, word, etc

INTEGER: [0-9]+;

INTEGER_LIST: [0-9]+ (',' [0-9]+)*;

DOUBLE: [0-9]+ ('.' [0-9]+)?;

FILENAME: [A-Za-z0-9]+ '.' [A-Za-z0-9]+;

VECTOR_3D: [0-9]+ ',' [0-9]+ ',' [0-9]+;

PATH: VECTOR_3D (';' VECTOR_3D)*;
