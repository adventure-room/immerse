grammar ImmerseAdventureModule;

action: playAudioAction | playBackgroundMusicAction | stopBackgroundMusicAction;

playAudioAction: resourceSection volumeSection?;

resourceSection: 'play ' filename=FILENAME;

volumeSection: ' at volume ' volume=INTEGER;


playBackgroundMusicAction: backgroundResourceSection volumeSection?;

backgroundResourceSection: 'play background music ' filename=FILENAME;

stopBackgroundMusicAction: 'stop background music';

// TODO: Use fragments for number, word, etc

INTEGER: [0-9]+;

DOUBLE: [0-9]+ ('.' [0-9]+)?;

FILENAME: [A-Za-z0-9]+ '.' [A-Za-z0-9]+;
