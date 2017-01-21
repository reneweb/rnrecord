#import <Foundation/Foundation.h>
#import "RCTBridgeModule.h"

@interface FindCommand : NSObject

-(void)find:(NSString *)tableName query:(NSDictionary *)query resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;
@end
