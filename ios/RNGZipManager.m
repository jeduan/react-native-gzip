#import "RNGZipManager.h"

@implementation RNGZipManager

RCT_EXPORT_MODULE()

RCT_REMAP_METHOD(gunzip,
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *destinationFolder = [paths firstObject];
    RCTLogInfo(@"Destination folder: %@", destinationFolder);

    NSURL *url = [[NSBundle mainBundle] URLForResource:@"foo" withExtension:@"tgz"];
    NSURL *tarPath = [NSURL URLWithString:[destinationFolder stringByAppendingString:@"/temp.tar"]];

    NSError *err;
    if (![fileManager GZipDecompressFile:url writingContentsToFile:tarPath error:&err]) {
        reject(@"gzip", @"error on decompression", err);
        return;
    }
    if (![DCTar decompressFileAtPath:[tarPath absoluteString] toPath:destinationFolder error:&err]) {
        reject(@"untar", @"error on untar", err);
        return;
    }
    resolve(@{@"path": destinationFolder});
}

@end
