/**
 * @providesModule RNGZip
 * @flow
 */
'use strict'

var NativeRNGZip = require('react-native').NativeModules.RNGZipManager

exports.gunzip = function gunzip (file, destinationFolder, force = false) {
  return NativeRNGZip.gunzip(file, destinationFolder, force)
}

exports.gunzipForce = function gunzip (file, destinationFolder) {
  return NativeRNGZip.gunzip(file, destinationFolder, true)
}
