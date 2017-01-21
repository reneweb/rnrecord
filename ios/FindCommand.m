#import "FindCommand.h"
#import "RnRecordSQLite.h"

@implementation FindCommand

-(void) find:(NSString *)tableName query:(NSDictionary *)query resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject {
    NSString *queryString = [self buildQuery:tableName query:query];
    NSArray *queryArguments = [self buildQueryArgument:query];
    
    [self executeDbCallsAsync:queryString queryArguments:queryArguments resolver:resolve rejecter:reject];
}

-(void) executeDbCallsAsync:(NSString *)queryString queryArguments:(NSArray *) queryArguments resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject {
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        RnRecordSQLite *rnRecordSQLite = [RnRecordSQLite getInstance];
        
        NSArray *result = [self runQuery:queryString withArguments:queryArguments withSQLite:rnRecordSQLite];
        resolve(result);
    });
}

-(NSArray *) runQuery:(NSString *)query withArguments:(NSArray *)queryArguments withSQLite:(RnRecordSQLite *)rnRecordSQLite {
    sqlite3 *db = [rnRecordSQLite openDB];
    sqlite3_stmt *statement = [rnRecordSQLite prepareStatement:query withArguments:queryArguments withDB:db];
    
    NSMutableArray *result = [[NSMutableArray alloc]init];
    while (sqlite3_step(statement) == SQLITE_ROW) {
        int columnCount = sqlite3_column_count(statement);
        NSMutableDictionary *row = [[NSMutableDictionary alloc]init];
        
        for (int i = 0; i < columnCount; i++) {
            NSString *columnName = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_name(statement, i)];
            NSString *currEntry = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, i)];
            [row setObject:currEntry forKey:columnName];
        }
        
        [result addObject:row];
    }
    sqlite3_reset(statement);
    
    return result;
}

-(NSArray *) buildQueryArgument:(NSDictionary *) query {
    NSArray *keys = [query allKeys];
    NSMutableArray *arguments = [[NSMutableArray alloc] init];
    
    for (int i = 0; i < [keys count]; i++) {
        NSObject *val = [query valueForKey:keys[i]];
        [arguments addObject:val];
    }
    
    return arguments;
}

-(NSString *) buildQuery:(NSString *) tableName query:(NSDictionary *) query {
    NSArray *keys = [query allKeys];
    NSMutableString *queryBuilder = [NSMutableString stringWithFormat:@"SELECT * from %@ WHERE ", tableName];
    for (int i = 0; i < [keys count]; i++) {
        NSString *key = keys[i];
        [queryBuilder appendString:key];
        [queryBuilder appendString:@" = ? "];
        if(i < [keys count] - 1) {
            [queryBuilder appendString:@"AND "];
        }
    }
    [queryBuilder appendString:@";"];
    return queryBuilder;
}
@end
