#import "RnRecord.h"

#import "DeleteCommand.h"
#import "FindCommand.h"
#import "FindAllCommand.h"
#import "SaveCommand.h"
#import "UpdateCommand.h"
#import "RnRecordSQLite.h"

@implementation RnRecord
{
    DeleteCommand * deleteCommand;
    FindCommand * findCommand;
    FindAllCommand * findAllCommand;
    SaveCommand * saveCommand;
    UpdateCommand * updateCommand;
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(save:(NSString *)tableName props:(NSDictionary *)props resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    if(saveCommand == nil) {
        saveCommand = [SaveCommand new];
    }
    
    [saveCommand save:tableName props:props resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(update:(NSString *)tableName props:(NSDictionary *)props resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    if(updateCommand == nil) {
        updateCommand = [UpdateCommand new];
    }
    
    [updateCommand update:tableName props:props resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(remove:(NSString *)tableName props:(NSDictionary *)props resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    if(deleteCommand == nil) {
        deleteCommand = [DeleteCommand new];
    }
    
    [deleteCommand delete:tableName props:props resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(findAll:(NSString *)tableName resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    if(findAllCommand == nil) {
        findAllCommand = [FindAllCommand new];
    }
    
    [findAllCommand findAll:tableName resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(find:(NSString *)tableName query:(NSDictionary *)query resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    if(findCommand == nil) {
        findCommand = [FindCommand new];
    }
    
    [findCommand find:tableName query:query resolver:resolve rejecter:reject];
}

@end
