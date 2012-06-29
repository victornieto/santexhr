// *********************************************************************************
// StringDelta
// 1. Calculates deltas between strings. 
// 2. Reconstructs strings using deltas.
// 3. Assumes the difference between the strings are minimal.
// 4. Runtime and memory usage (in case of #3) is linear.
//
// @author	Ali Shahriyari
//
// *********************************************************************************

// Vars
var StringDelta = {};
var _baseString = "";

// *********************************************************************************
// User Methods
// *********************************************************************************

// Set the initial string to start calculating deltas against
StringDelta.setBase = function( baseString )
{
  _baseString = baseString;
}

// Get delta
StringDelta.getDelta = function( modifiedString )
{
  return calculateDelta( modifiedString ); 
}

// Get string
StringDelta.getString = function( delta )
{
  return reconstructString( delta );
}

// *********************************************************************************
// Private Methods
// *********************************************************************************

// Calculates delta
// Output format: deltaStart, deltaChange, delta (if any)
// Assumption: Small change between new string and old string
function calculateDelta( modifiedString )
{
  var deltaStart = 0;
  var deltaChange = 0;
  var delta = "";

  // Determine loop count length 
  var loopLength = _baseString.length;
  if ( modifiedString.length < loopLength )
    loopLength = modifiedString.length;
 
  // Determine deltaStart
  for ( ; deltaStart < loopLength; deltaStart++ )
  {
    if ( modifiedString.charAt( deltaStart ) != _baseString.charAt( deltaStart ))
      break;
  }
  
  // Determine deltaChange
  loopLength -= deltaStart;
  var baseCounter = _baseString.length - 1;
  var modCounter = modifiedString.length - 1;
  var i = 0;
  for ( ; i < loopLength; i++, baseCounter--, modCounter-- )
  {
    if ( modifiedString.charAt( modCounter ) != _baseString.charAt( baseCounter ))
      break;
  }
  deltaChange = _baseString.length - deltaStart - i; 

  // Determine delta
  delta = modifiedString.substr( deltaStart, modCounter - deltaStart + 1 );

  // Set modified string to base string
  _baseString = modifiedString; 

  // Calcualte delta string
  var deltaString = deltaStart + ",";
  if ( deltaChange > 0 )
    deltaString += deltaChange + ",";
  else 
    deltaString += ",";
  deltaString += delta;

  // Return delta string
  return deltaString;
}

// Reconstructs using delta
function reconstructString( deltaString )
{
  var newString = "";

  // Get deltaStart 
  var deltaStartLength = deltaString.indexOf( "," );
  var deltaStart = parseInt( deltaString.substr( 0, deltaStartLength ));
  
  // Get deltaChange
  var deltaChangeStart = deltaStartLength + 1;
  var deltaChangeLength = deltaString.indexOf( ",", deltaChangeStart ) - deltaChangeStart;
  var deltaChange = 0;
  if ( deltaChangeLength != 0 )
    deltaChange = parseInt( deltaString.substr( deltaChangeStart, deltaChangeLength ));

  // Get delta 
  var delta = deltaString.substr( deltaChangeStart + deltaChangeLength + 1 );
  
  // Make new string
  newString = _baseString.substr( 0, deltaStart );
  newString += delta;
  newString += _baseString.substr( deltaStart + deltaChange ); 

  // Set new string to base string
  _baseString = newString; 

  // Return new string
  return newString;
}
