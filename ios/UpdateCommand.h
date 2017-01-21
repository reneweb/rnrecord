#import <Foundation/Foundation.h>
#import "RCTBridgeModule.h"

@interface UpdateCommand : NSObject

-(void)update:(NSString *)tableName props:(NSDictionary *)props resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;
@end
