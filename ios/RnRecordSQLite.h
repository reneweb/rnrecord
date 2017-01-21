#import <Foundation/Foundation.h>
#import <sqlite3.h>

@interface RnRecordSQLite : NSObject

+(RnRecordSQLite*)getInstance;
-(sqlite3 *) openDB;
-(sqlite3_stmt *)prepareStatement:(NSString *)sql withDB:(sqlite3 *)db;

-(sqlite3_stmt *)prepareStatement:(NSString *)sql withArguments:(NSArray *)arguments withDB:(sqlite3 *)db;

@end
