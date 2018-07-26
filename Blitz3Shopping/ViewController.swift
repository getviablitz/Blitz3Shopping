//
//  ViewController.swift
//  Blitz3Shopping
//
//  Created by Sivaprasad Masina on 26/07/18.
//  Copyright © 2018 BlitzIt. All rights reserved.
//

import UIKit

var webUrlString = 0
var socialMediaIndexes = [Int]()

struct ScreenSize {
    static let SCREEN_WIDTH = UIScreen.main.bounds.size.width
    static let SCREEN_HEIGHT = UIScreen.main.bounds.size.height
    static let SCREEN_MAX_LENGTH = max(ScreenSize.SCREEN_WIDTH, ScreenSize.SCREEN_HEIGHT)
    static let SCREEN_MIN_LENGTH = min(ScreenSize.SCREEN_WIDTH, ScreenSize.SCREEN_HEIGHT)
}

struct DeviceType {
    static let IS_IPHONE_4_OR_LESS =  UIDevice.current.userInterfaceIdiom == .phone && ScreenSize.SCREEN_MAX_LENGTH < 568.0
    static let IS_IPHONE_5 = UIDevice.current.userInterfaceIdiom == .phone && ScreenSize.SCREEN_MAX_LENGTH == 568.0
    static let IS_IPHONE_6 = UIDevice.current.userInterfaceIdiom == .phone && ScreenSize.SCREEN_MAX_LENGTH == 667.0
    static let IS_IPHONE_6P = UIDevice.current.userInterfaceIdiom == .phone && ScreenSize.SCREEN_MAX_LENGTH == 736.0
    static let IS_IPAD_MINI = UIDevice.current.userInterfaceIdiom == .pad && ScreenSize.SCREEN_MAX_LENGTH == 1024.0
    static let IS_IPAD_PRO = UIDevice.current.userInterfaceIdiom == .pad && ScreenSize.SCREEN_MAX_LENGTH == 1366.0
}

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, lsmviewdelegate {
    
    @IBOutlet var listCloseButton: UIButton!
    @IBOutlet var ListTableView: UITableView!
    @IBOutlet var defaulImageView: UIImageView!
    @IBOutlet var gobutton: UIButton!
    @IBOutlet var menuButton: UIButton!
    
    var tempList = ["Best-Buy", "Amazon", "Alibaba", "Walmart", "Costco", "NordStrom"]
    var listArray = [ListObject]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        //self.view.backgroundColor = UIColor(patternImage: UIImage(named : "Default.png")!)
        self.listCloseButton.frame.origin.x = (self.ListTableView.frame.origin.x + self.ListTableView.frame.size.width) - 25
        self.listCloseButton.frame.origin.y = (self.ListTableView.frame.origin.y) - 25
        self.ListTableView.isHidden = true
        self.listCloseButton.isHidden = true
        
        self.listArray.append(ListObject(listitem: "Best-Buy", isNew: false))
        self.listArray.append(ListObject(listitem: "Amazon", isNew: false))
        self.listArray.append(ListObject(listitem: "Alibaba", isNew: false))
        self.listArray.append(ListObject(listitem: "Walmart", isNew: false))
        self.listArray.append(ListObject(listitem: "Costco", isNew: false))
        self.listArray.append(ListObject(listitem: "NordStrom", isNew: false))
        
        if(UserDefaults.standard.value(forKey: "socialmedia") != nil) {
            var socialmedia = ""
            socialmedia = UserDefaults.standard.value(forKey: "socialmedia") as! String
            print(socialmedia)
            let alertArray = socialmedia.components(separatedBy: ",")
            print(alertArray)
            for i in alertArray{
                print(i)
                socialMediaIndexes.append(Int(i)!)
                self.listArray[Int(i)!].isNew = true
            }
        }else{
            socialMediaIndexes.append(0)
            socialMediaIndexes.append(1)
            socialMediaIndexes.append(2)
            self.listArray[0].isNew = true
            self.listArray[1].isNew = true
            self.listArray[2].isNew = true
        }
        
    }
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.isNavigationBarHidden = true
    }
    override func viewDidAppear(_ animated: Bool) {
        
        if(DeviceType.IS_IPHONE_4_OR_LESS){
            defaulImageView.image = UIImage(named: "Default.png");
        }
        
        if(DeviceType.IS_IPHONE_5){
            defaulImageView.image = UIImage(named: "Default-568h@2x~iphone.png");
        }
        
        if(DeviceType.IS_IPHONE_6){
            defaulImageView.image = UIImage(named: "Default-375w-667h@2x~iphone.png");
        }
        
        if(DeviceType.IS_IPHONE_6P){
            defaulImageView.image = UIImage(named: "Default-414w-736h@3x~iphone.png");
        }
        if(DeviceType.IS_IPAD_MINI){
            defaulImageView.image = UIImage(named: "1.png");
        }
        
        NotificationCenter.default.addObserver(self, selector: #selector(ViewController.Rotated), name: NSNotification.Name.UIDeviceOrientationDidChange, object: nil)
    }
    override func viewDidDisappear(_ animated: Bool) {
        NotificationCenter.default.removeObserver(self, name:NSNotification.Name(rawValue: "UIDeviceOrientationDidChangeNotification"), object:nil)
    }
    override var supportedInterfaceOrientations : UIInterfaceOrientationMask {
        let orientation: UIInterfaceOrientationMask = [UIInterfaceOrientationMask.portrait, UIInterfaceOrientationMask.portraitUpsideDown]
        return orientation
    }
    
    override var shouldAutorotate : Bool {
        return false
    }
    @objc func Rotated(){
        if(UIDevice.current.orientation == UIDeviceOrientation.landscapeLeft || UIDevice.current.orientation == UIDeviceOrientation.landscapeRight){
            let lsmViewControler = self.storyboard?.instantiateViewController(withIdentifier: "LSMId") as! LandScapeViewController
            if(UIDevice.current.orientation == UIDeviceOrientation.landscapeLeft){
                lsmViewControler.orientationLSMId = "right"
            }else if(UIDevice.current.orientation == UIDeviceOrientation.landscapeRight){
                lsmViewControler.orientationLSMId = "left"
            }
            lsmViewControler.delegate = self
            lsmViewControler.filterArray = socialMediaIndexes
            self.navigationController?.present(lsmViewControler, animated: true, completion: nil)
        }else if(UIDevice.current.orientation == UIDeviceOrientation.portrait){
            
        }
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func ListCloseButtonAction(_ sender: UIButton) {
        self.ListTableView.isHidden = true
        self.listCloseButton.isHidden = true
    }
    @IBAction func GoAction(_ sender: UIButton) {
        let lsmViewControler = self.storyboard?.instantiateViewController(withIdentifier: "LSMId") as! LandScapeViewController
        lsmViewControler.orientationLSMId = "left"
        lsmViewControler.filterArray = socialMediaIndexes
        lsmViewControler.delegate = self
        self.navigationController?.present(lsmViewControler, animated: true, completion: nil)
    }
    @IBAction func MenuAction(_ sender: UIButton) {
        self.ListTableView.isHidden = false
        self.listCloseButton.isHidden = false
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int{
        return self.listArray.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell{
        
        var cell = tableView.dequeueReusableCell(withIdentifier: "cellid") as? ListCustomTableViewCell
        
        if(cell == nil){
            cell = ListCustomTableViewCell(style: UITableViewCellStyle.default, reuseIdentifier: "cellid")
        }
        cell?.selectionStyle = UITableViewCellSelectionStyle.none
        cell?.listLabel.text = self.listArray[indexPath.row].listitem
        cell?.checkButton.addTarget(self, action: #selector(ViewController.CheckButtonAction(_:)), for: UIControlEvents.touchUpInside)
        cell?.checkButton.tag = indexPath.row
        if(self.listArray[indexPath.row].isNew == true){
            cell?.checkButton.isSelected = true
        }else if(self.listArray[indexPath.row].isNew == false){
            cell?.checkButton.isSelected = false
        }
        return cell!
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat{
        return 80
    }
    @objc func CheckButtonAction(_ sender: UIButton) {
        if(sender.isSelected == false){
            sender.isSelected = true
            self.listArray[socialMediaIndexes[0]].isNew = false
            socialMediaIndexes.removeFirst()
            socialMediaIndexes.append(sender.tag)
            self.listArray[sender.tag].isNew = true
            var totalarray = "\(socialMediaIndexes[0])"
            totalarray = totalarray + "," + "\(socialMediaIndexes[1])"
            totalarray = totalarray + "," + "\(socialMediaIndexes[2])"
            UserDefaults.standard.set(totalarray, forKey: "socialmedia")
        }
        self.ListTableView.reloadData()
    }
    
    func GotoWebView(_ url: String) {
        let webview = self.storyboard?.instantiateViewController(withIdentifier: "webid") as! WebViewController
        webview.urlString = url
        self.navigationController?.pushViewController(webview, animated: false)
    }
    func GotoRootView() {
        print("GotoRootView")
    }
}

