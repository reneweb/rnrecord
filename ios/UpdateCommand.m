#import "UpdateCommand.h"
#import "RnRecordSQLite.h"
#import "RCTUtils.h"

@implementation UpdateCommand

-(void) update:(NSString *)tableName props:(NSDictionary *)props resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject {
    NSString *queryString = [self buildQuery:tableName withProps:props];
    [self executeDbCallsAsync:queryString withProps:props resolver:resolve rejecter:reject];
}

-(void) executeDbCallsAsync:(NSString *)queryString withProps:(NSDictionary *)props resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject {
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        NSMutableDictionary *arguments = [props mutableCopy];
        [arguments removeObjectForKey:@"id"];
        
        RnRecordSQLite *rnRecordSQLite = [RnRecordSQLite getInstance];
        sqlite3 *db = [rnRecordSQLite openDB];
        sqlite3_stmt *statement = [rnRecordSQLite prepareStatement:queryString withArguments:[arguments allValues] withDB:db];
        if (sqlite3_step(statement) == SQLITE_DONE) {
            resolve(@(sqlite3_changes(db)));
        } else {
            reject(RCTErrorUnspecified, @"Could not update entry.", nil);
        }
        
        sqlite3_reset(statement);
    });
}

-(NSString *)buildQuery:(NSString *)tableName withProps:(NSDictionary *)props {
    NSMutableDictionary *queryProps = [props mutableCopy];
    [queryProps removeObjectForKey:@"id"];
    
    NSArray *keys = [queryProps allKeys];
    NSMutableString *queryBuilder = [NSMutableString stringWithFormat:@"UPDATE %@ SET ", tableName];
    for (int i = 0; i < [keys count]; i++) {
        NSString *key = keys[i];
        if([key isEqualToString:@"id"]) continue;
        
        [queryBuilder appendString:key];
        [queryBuilder appendString:@" = ?"];
        if(i < [keys count] - 1) {
            [queryBuilder appendString:@", "];
        }
    }
    [queryBuilder appendString:[NSMutableString stringWithFormat:@" WHERE id = %@", props[@"id"]]];
    [queryBuilder appendString:@";"];
    return queryBuilder;
    
}

@end
