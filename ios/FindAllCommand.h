#import <Foundation/Foundation.h>
#import "RCTBridgeModule.h"

@interface FindAllCommand : NSObject

-(void)findAll:(NSString *)tableName resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;
@end
