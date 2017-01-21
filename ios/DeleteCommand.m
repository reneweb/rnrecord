#import "DeleteCommand.h"
#import "RnRecordSQLite.h"
#import "RCTUtils.h"
#import <sqlite3.h>

@implementation DeleteCommand

-(void)delete:(NSString *)tableName props:(NSDictionary *)props resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject {
    NSInteger *elemId = [props[@"id"] integerValue];
    [self executeDbCallsAsync:tableName withId:elemId resolver:resolve rejecter:reject];
}

-(void) executeDbCallsAsync:(NSString *)tableName withId:(NSInteger *)elemId resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject {
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        RnRecordSQLite *rnRecordSQLite = [RnRecordSQLite getInstance];
        sqlite3 *db = [rnRecordSQLite openDB];
        NSString *deleteSQL = [NSString stringWithFormat:@"DELETE FROM %@ WHERE id = '%@'", tableName, elemId];
        sqlite3_stmt *statement = [rnRecordSQLite prepareStatement:deleteSQL withDB:db];
        
        if (sqlite3_step(statement) == SQLITE_DONE) {
            resolve(@(sqlite3_changes(db)));
        } else {
            reject(RCTErrorUnspecified, @"Could not delete entry.", nil);
        }
        
        
        sqlite3_reset(statement);
    });
}

@end
