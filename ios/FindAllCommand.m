#import "FindAllCommand.h"
#import "RnRecordSQLite.h"

@implementation FindAllCommand

-(void) findAll:(NSString *)tableName resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject {
    [self executeDbCallsAsync:tableName resolver:resolve rejecter:reject];
}

-(void)executeDbCallsAsync:(NSString *)tableName resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject {
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        RnRecordSQLite *rnRecordSQLite = [RnRecordSQLite getInstance];
        resolve([self runQuery:tableName withSQLite:rnRecordSQLite]);
    });
}

-(NSArray *)runQuery:(NSString *) tableName withSQLite:(RnRecordSQLite *)rnRecordSQLite {
    sqlite3 *db = [rnRecordSQLite openDB];
    NSString *findSQL = [NSString stringWithFormat:@"SELECT * FROM %@;", tableName];
    sqlite3_stmt *statement = [rnRecordSQLite prepareStatement:findSQL withDB:db];
    
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

@end
