grammar ImmerseAdventureModule;

action: playAudioAction;

playAudioAction: resourceSection volumeSection;

resourceSection: 'play ' filename=FILENAME;

volumeSection: ' at ' volume=INTEGER;

// TODO: Use fragments for number, word, etc

INTEGER: [0-9]+;

DOUBLE: [0-9]+ ('.' [0-9]+)?;

FILENAME: [A-Za-z0-9]+ '.' [A-Za-z0-9]+;
