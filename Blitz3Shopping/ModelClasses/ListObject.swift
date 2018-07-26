//
//  ListObject.swift
//  Blitz3Social
//
//  Created by Sivaprasad Masina on 25/07/18.
//  Copyright © 2018 BlitzIt. All rights reserved.
//

import UIKit

class ListObject: NSObject {

    var listitem:String
    var isNew:Bool
    init(listitem:String,isNew:Bool) {
        self.listitem = listitem
        self.isNew = isNew
    }
}
