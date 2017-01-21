#import <Foundation/Foundation.h>
#import "RCTBridgeModule.h"

@interface DeleteCommand : NSObject

-(void)delete:(NSString *)tableName props:(NSDictionary *)props resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;
@end
