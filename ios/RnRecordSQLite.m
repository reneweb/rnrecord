#import "RnRecordSQLite.h"
#import <Foundation/Foundation.h>
#import <sqlite3.h>

static RnRecordSQLite *sharedInstance = nil;

@implementation RnRecordSQLite
{
    NSString *databasePath;
}

+(RnRecordSQLite*)getInstance{
    if (!sharedInstance) {
        sharedInstance = [[super allocWithZone:NULL]init];
    }
    return sharedInstance;
}

-(id)init {
    self = [super init];
    if(self) {
        NSString *docsDir;
        NSArray *dirPaths;
        // Get the documents directory
        dirPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        docsDir = dirPaths[0];
        // Build the path to the database file
        databasePath = [[NSString alloc] initWithString:[docsDir stringByAppendingPathComponent: @"RnRecord.db"]];
    }
    return self;
}

-(sqlite3 *)openDB {
    const char *dbpath = [databasePath UTF8String];
    sqlite3 *database = nil;
    if (sqlite3_open(dbpath, &database) == SQLITE_OK) {
        return database;
    } else {
        return nil;
    }
}

-(sqlite3_stmt *)prepareStatement:(NSString *)sql withDB:(sqlite3 *)db {
    sqlite3_stmt *statement = nil;
    sqlite3_prepare_v2(db, [sql UTF8String], -1, &statement, NULL);
    return statement;
}

-(sqlite3_stmt *)prepareStatement:(NSString *)sql withArguments:(NSArray *)arguments withDB:(sqlite3 *)db {
    sqlite3_stmt *statement = nil;
    sqlite3_prepare_v2(db, [sql UTF8String], -1, &statement, NULL);
    
    for (int i = 0; i < [arguments count]; i++) {
        NSObject *val = arguments[i];
        if([val isKindOfClass:[NSNumber class]]) {
            sqlite3_bind_double(statement, i + 1, [(NSNumber *)val doubleValue]);
        } else if([val isKindOfClass:[NSString class]]) {
            sqlite3_bind_text(statement, i + 1, [(NSString *)val UTF8String], -1, NULL);
        }
    }
    
    return statement;
}

@end
