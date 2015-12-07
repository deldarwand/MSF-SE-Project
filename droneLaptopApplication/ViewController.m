//
//  ViewController.m
//  droneLaptopApplication
//
//  Created by Daniel Eldar on 02/12/2015.
//  Copyright © 2015 Daniel Eldar. All rights reserved.
//

#import "ViewController.h"


@interface ViewController()
@property (weak) IBOutlet NSTextField *dataToSendField;
@property CBPeripheralManager* pm;
@property NSData* dataToSend;
@property CBMutableCharacteristic* chara;
@end

@implementation ViewController
- (IBAction)sendDataAction:(id)sender
{
    NSString* stringToSend = self.dataToSendField.stringValue;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.pm = [[CBPeripheralManager alloc] initWithDelegate:self queue:nil];

    
    // Do any additional setup after loading the view.
}

- (void)peripheralManagerDidUpdateState:(CBPeripheralManager *)peripheral
{
    CBUUID* customUUID = [CBUUID UUIDWithString:@"5626FBDF-AC4C-44EC-A8E0-61E260A96AAE"];
    CBUUID* serviceUUID = [CBUUID UUIDWithString:@"4DD2F25E-7B40-424E-936B-953B671BCCAB"];
    self.dataToSend = [self.dataToSendField.stringValue dataUsingEncoding:NSUTF8StringEncoding];
    
    
    self.chara = [[CBMutableCharacteristic alloc] initWithType:customUUID properties:CBCharacteristicPropertyRead value:self.dataToSend permissions:CBAttributePermissionsReadable];
    
    CBMutableService* myService = [[CBMutableService alloc] initWithType:serviceUUID primary:YES];
    myService.characteristics = @[self.chara];
    
    [self.pm addService:myService];
}

- (void)peripheralManager:(CBPeripheralManager *)peripheral central:(CBCentral *)central didSubscribeToCharacteristic:(CBCharacteristic *)characteristic
{
    
}

- (void)peripheralManagerDidStartAdvertising:(CBPeripheralManager *)peripheral
                                       error:(NSError *)error {
    
    if (error) {
        NSLog(@"Error advertising: %@", [error localizedDescription]);
    }
}

- (void)peripheralManager:(CBPeripheralManager *)peripheral
            didAddService:(CBService *)service
                    error:(NSError *)error {
    
    if (error) {
        NSLog(@"Error publishing service: %@", [error localizedDescription]);
    }
    
    [self.pm startAdvertising:@{ CBAdvertisementDataServiceUUIDsKey :
                                                 @[service.UUID] }];
}

- (void)peripheralManager:(CBPeripheralManager *)peripheral
    didReceiveReadRequest:(CBATTRequest *)request {
    
    if ([request.characteristic.UUID isEqual:@"5626FBDF-AC4C-44EC-A8E0-61E260A96AAE"]) {
        if (request.offset > self.chara.value.length) {
            [self.pm respondToRequest:request
                                       withResult:CBATTErrorInvalidOffset];
            return;
        }
        request.value = [self.chara.value
                        subdataWithRange:NSMakeRange(request.offset,
                        self.chara.value.length - request.offset)];
    }
    [self.pm respondToRequest:request withResult:CBATTErrorSuccess];
}

- (void)setRepresentedObject:(id)representedObject {
    [super setRepresentedObject:representedObject];

    // Update the view, if already loaded.
}

@end
