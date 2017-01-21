#import "SaveCommand.h"
#import "RnRecordSQLite.h"
#import "RCTUtils.h"

@implementation SaveCommand

-(void) save:(NSString *)tableName props:(NSDictionary *)props resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject {
    
    NSString *createQuery = [self buildCreateQuery:tableName withProps:props];
    NSString *insertQuery = [self buildInsertQuery:tableName withProps:props];
    [self executeDbCallsAsync:createQuery withInsertQuery:insertQuery withProps:props resolver:resolve rejecter:reject];
}

-(void) executeDbCallsAsync:(NSString *)createQuery withInsertQuery:(NSString *)insertQuery withProps:(NSDictionary *)props resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject {
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        RnRecordSQLite *rnRecordSQLite = [RnRecordSQLite getInstance];
        sqlite3 *db = [rnRecordSQLite openDB];
        sqlite3_stmt *createStatement = [rnRecordSQLite prepareStatement:createQuery withDB:db];
        
        if (sqlite3_step(createStatement) != SQLITE_DONE) {
            reject(RCTErrorUnspecified, @"Failed to create db.", nil);
            sqlite3_reset(createStatement);
            return;
        }
        sqlite3_reset(createStatement);
        
        sqlite3_stmt *insertStatement = [rnRecordSQLite prepareStatement:insertQuery withArguments:[props allValues] withDB:db];
        if (sqlite3_step(insertStatement) == SQLITE_DONE) {
            resolve(@(sqlite3_changes(db)));
        } else {
            reject(RCTErrorUnspecified, @"Failed to insert data.", nil);
        }
        sqlite3_reset(insertStatement);
        
    });
}

-(NSString *) buildCreateQuery:(NSString *)tableName withProps:(NSDictionary *)props {
    NSArray *keys = [props allKeys];
    NSMutableString *queryBuilder = [NSMutableString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ ", tableName];
    [queryBuilder appendString:@"("];
    [queryBuilder appendString:@"id INTEGER PRIMARY KEY AUTOINCREMENT"];
    for (int i = 0; i < [keys count]; i++) {
        NSString *key = keys[i];
        NSString *propType = nil;
        NSObject *val = [props valueForKey:keys[i]];
        
        if([val isKindOfClass:[NSNumber class]]) {
            propType = @"DOUBLE";
        } else if([val isKindOfClass:[NSString class]]) {
            propType = @"TEXT";
        }
        
        if(propType != nil) {
            [queryBuilder appendString:@","];
            [queryBuilder appendString:key];
            [queryBuilder appendString:@" "];
            [queryBuilder appendString:propType];
        }
    }
    [queryBuilder appendString:@");"];
    return queryBuilder;
}

-(NSString *) buildInsertQuery:(NSString *)tableName withProps:(NSDictionary *)props {
    NSArray *keys = [props allKeys];
    NSMutableString *queryBuilder = [NSMutableString stringWithFormat:@"INSERT INTO %@ (", tableName];
    for (int i = 0; i < [keys count]; i++) {
        [queryBuilder appendString:keys[i]];
        if(i < [keys count] - 1) {
            [queryBuilder appendString:@", "];
        }
    }
    [queryBuilder appendString:@") VALUES("];
    for (int i = 0; i < [keys count]; i++) {
        [queryBuilder appendString:@"? "];
        if(i < [keys count] - 1) {
            [queryBuilder appendString:@", "];
        }
    }
    [queryBuilder appendString:@")"];
    return queryBuilder;
}

@end
