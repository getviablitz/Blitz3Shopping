//
//  ListCustomTableViewCell.swift
//  Blitz3Social
//
//  Created by Sivaprasad Masina on 24/07/18.
//  Copyright © 2018 BlitzIt. All rights reserved.
//

import UIKit

class ListCustomTableViewCell: UITableViewCell {

    @IBOutlet var checkButton: UIButton!
    @IBOutlet var listLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        self.checkButton.setImage(UIImage(named:"uncheckpref.png"), for: UIControlState.normal)
        self.checkButton.setImage(UIImage(named:"checkpref.png"), for: UIControlState.selected)
        // Configure the view for the selected state
    }
    
}
