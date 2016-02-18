/**
 * @providesModule RNGZip
 * @flow
 */
'use strict';

var NativeRNGZip = require('NativeModules').RNGZip

/**
 * High-level docs for the RNGZip iOS API can be written here.
 */

var ReactNativeGZip = {
  gunzip: NativeRNGZip.gunzip
}

module.exports = ReactNativeGZip
