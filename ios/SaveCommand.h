#import <Foundation/Foundation.h>
#import "RCTBridgeModule.h"

@interface SaveCommand : NSObject

-(void)save:(NSString *)tableName props:(NSDictionary *)props resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;
@end
