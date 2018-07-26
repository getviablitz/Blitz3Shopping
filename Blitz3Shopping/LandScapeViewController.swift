//
//  LandScapeViewController.swift
//  Blitz3Social
//
//  Created by Sivaprasad Masina on 24/07/18.
//  Copyright © 2018 BlitzIt. All rights reserved.
//

import UIKit

@objc protocol lsmviewdelegate{
    @objc optional func GotoWebView(_ url:String)
    @objc optional func GotoRootView()
}

class LandScapeViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UIWebViewDelegate  {
    

    var web1 = UIWebView()
    var web2 = UIWebView()
    var web3 = UIWebView()
    
    var filterfeedleft = UIButton()
    var fullviewfeedleft = UIButton()
    var filterfeedmiddle = UIButton()
    
    var fullviewfeedmiddle = UIButton()
    var filterfeedright = UIButton()
    var fullviewfeedright = UIButton()
    
    var feedLeftHeaderView = UIView()
    var feedMiddleHeaderView = UIView()
    var feedRightHeaderView = UIView()
    
    @IBOutlet var maskView: UIView!
    @IBOutlet var fullOverlayWebView: UIWebView!
    @IBOutlet var closeWebButton: UIButton!
    
    var leftListView = UITableView()
    var middleListView = UITableView()
    var rightListView = UITableView()
    
    var delegate:lsmviewdelegate?
    var orientationLSMId = "left"
    var tempList = ["Best-Buy", "Amazon", "Alibaba", "Walmart", "Costco", "NordStrom", "close"]
    var socialLinks = ["https://www.bestbuy.com", "https://www.amazon.com", "http://www.alibaba.com" , "https://www.walmart.com", "https://www.costco.com", "https://shop.nordstrom.com"]
    var fromView = ""
    var fromSubView = ""
    var filterArray = [Int]()
    var webToUrlString = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        var width = UIScreen.main.bounds.size.width
        var height = UIScreen.main.bounds.size.height
        if(self.fromSubView == "rotation"){
            width = UIScreen.main.bounds.size.height
            height = UIScreen.main.bounds.size.width
        }
        self.web1.frame = CGRect(x: height/3 * CGFloat(0), y: 40, width: height/3, height: width - 40)
        self.view.addSubview(self.web1)
        web1.scrollView.bounces = false
        web1.delegate = self
        web1.autoresizesSubviews = false
        web1.backgroundColor = .clear
        web1.isOpaque = false
        web1.scrollView.contentInset = UIEdgeInsets(top: -8, left: -8, bottom: -8, right: -8)
        
        self.web2.frame = CGRect(x: height/3 * CGFloat(1), y: 40, width: height/3, height: width - 40)
        self.view.addSubview(self.web2)
        web2.scrollView.bounces = false
        web2.delegate = self
        web2.autoresizesSubviews = false
        web2.backgroundColor = .clear
        web2.isOpaque = false
        web2.scrollView.contentInset = UIEdgeInsets(top: -8, left: -8, bottom: -8, right: -8)
        
        self.web3.frame = CGRect(x: height/3 * CGFloat(2), y: 40, width: height/3, height: width - 40)
         self.view.addSubview(self.web3)
        web3.scrollView.bounces = false
        web3.delegate = self
        web3.autoresizesSubviews = false
        web3.backgroundColor = .clear
        web3.isOpaque = false
        web3.scrollView.contentInset = UIEdgeInsets(top: -8, left: -8, bottom: -8, right: -8)
        
        self.feedLeftHeaderView.frame = CGRect(x: height/3 * CGFloat(0), y: 0, width: height/3, height: 40)
        self.view.addSubview(self.feedLeftHeaderView)
        self.feedLeftHeaderView.backgroundColor = UIColor.blue
        
        self.feedMiddleHeaderView.frame = CGRect(x: height/3 * CGFloat(1), y: 0, width: height/3, height: 40)
        self.view.addSubview(self.feedMiddleHeaderView)
        self.feedMiddleHeaderView.backgroundColor = UIColor.orange
        
        self.feedRightHeaderView.frame = CGRect(x: height/3 * CGFloat(2), y: 0, width: height/3, height: 40)
        self.view.addSubview(self.feedRightHeaderView)
        self.feedRightHeaderView.backgroundColor = UIColor.purple
        // Do any additional setup after loading the view.
        
        filterfeedleft.frame = CGRect(x: 0, y: 5, width: 30, height: 30)
        filterfeedleft.setBackgroundImage(UIImage(named: "menu.png"), for: UIControlState())
        filterfeedleft.addTarget(self, action: #selector(LandScapeViewController.FilterFeedLeftAction(_:)), for: UIControlEvents.touchUpInside)
        feedLeftHeaderView.addSubview(filterfeedleft)
        
        fullviewfeedleft.frame = CGRect(x: feedLeftHeaderView.frame.size.width - 40, y: 5, width: 30, height: 30)
        fullviewfeedleft.setBackgroundImage(UIImage(named: "expand.png"), for: UIControlState())
        fullviewfeedleft.addTarget(self, action: #selector(LandScapeViewController.FullViewFeedLeftAction(_:)), for: UIControlEvents.touchUpInside)
        feedLeftHeaderView.addSubview(fullviewfeedleft)
        
        filterfeedmiddle.frame = CGRect(x: 0, y: 5, width: 30, height: 30)
        filterfeedmiddle.setBackgroundImage(UIImage(named: "menu.png"), for: UIControlState())
        filterfeedmiddle.addTarget(self, action: #selector(LandScapeViewController.FilterFeedMiddleAction(_:)), for: UIControlEvents.touchUpInside)
        feedMiddleHeaderView.addSubview(filterfeedmiddle)
        
        fullviewfeedmiddle.frame = CGRect(x: feedMiddleHeaderView.frame.size.width - 40, y: 5, width: 30, height: 30)
        fullviewfeedmiddle.setBackgroundImage(UIImage(named: "expand.png"), for: UIControlState())
        fullviewfeedmiddle.addTarget(self, action: #selector(LandScapeViewController.FullViewFeedMiddleAction(_:)), for: UIControlEvents.touchUpInside)
        feedMiddleHeaderView.addSubview(fullviewfeedmiddle)
        
        filterfeedright.frame = CGRect(x:0, y: 5, width: 30, height: 30)
        filterfeedright.setBackgroundImage(UIImage(named: "menu.png"), for: UIControlState())
        filterfeedright.addTarget(self, action: #selector(LandScapeViewController.FilterFeedRightAction(_:)), for: UIControlEvents.touchUpInside)
        feedRightHeaderView.addSubview(filterfeedright)
        
        fullviewfeedright.frame = CGRect(x: feedRightHeaderView.frame.size.width - 40, y: 5, width: 30, height: 30)
        fullviewfeedright.setBackgroundImage(UIImage(named: "expand.png"), for: UIControlState())
        fullviewfeedright.addTarget(self, action: #selector(LandScapeViewController.FullViewFeedRightAction(_:)), for: UIControlEvents.touchUpInside)
        feedRightHeaderView.addSubview(fullviewfeedright)
        
        self.automaticallyAdjustsScrollViewInsets = false
        DispatchQueue.main.async(execute: {
            if let url = URL(string: self.socialLinks[self.filterArray[0]]) {
                let request = URLRequest(url: url)
                self.web1.loadRequest(request)
            }
            // web2.delegate = self as! UIWebViewDelegate
            if let url = URL(string: self.socialLinks[self.filterArray[1]]) {
                let request = URLRequest(url: url)
                self.web2.loadRequest(request)
            }
            //  web3.delegate = self as! UIWebViewDelegate
            if let url = URL(string: self.socialLinks[self.filterArray[2]]) {
                let request = URLRequest(url: url)
                self.web3.loadRequest(request)
            }
        })
        
        if(self.fromView == "web"){
            self.maskView.isHidden = false
            self.view.bringSubview(toFront: self.maskView)
            let urlString = webToUrlString
            self.fullOverlayWebView.tag = webUrlString
            if(webUrlString == 1){
                if let url = URL(string: urlString) {
                    let request = URLRequest(url: url)
                    web1.loadRequest(request)                }
            }else if(webUrlString == 2){
                if let url = URL(string: urlString) {
                    let request = URLRequest(url: url)
                    web2.loadRequest(request)
                }
            }else if(webUrlString == 3){
                if let url = URL(string: urlString) {
                    let request = URLRequest(url: url)
                    web3.loadRequest(request)
                }
            }
            DispatchQueue.main.async(execute: {
                let requestURL = URL(string:urlString)
                if(requestURL != nil){
                    let request = URLRequest(url: requestURL!)
                    self.fullOverlayWebView.loadRequest(request)
                }else{
                    let requestURL = URL(string:"https://www.google.com")
                    if(requestURL != nil){
                        let request = URLRequest(url: requestURL!)
                        self.fullOverlayWebView.loadRequest(request)
                    }
                }
            })
        }else{
            self.maskView.isHidden = true
        }
        self.SetListViews()
        self.SetMiddleSwipeViews()
    }
    override func viewWillAppear(_ animated: Bool) {
        if #available(iOS 11, *) {
            let guide = view.safeAreaLayoutGuide
            NSLayoutConstraint.activate([
                web1.topAnchor.constraintEqualToSystemSpacingBelow(guide.topAnchor, multiplier: 0.0),
                guide.bottomAnchor.constraintEqualToSystemSpacingBelow(web1.bottomAnchor, multiplier: 5.0),
                web2.topAnchor.constraintEqualToSystemSpacingBelow(guide.topAnchor, multiplier: 0.0),
                guide.bottomAnchor.constraintEqualToSystemSpacingBelow(web2.bottomAnchor, multiplier: 5.0),
                web3.topAnchor.constraintEqualToSystemSpacingBelow(guide.topAnchor, multiplier: 0.0),
                guide.bottomAnchor.constraintEqualToSystemSpacingBelow(web3.bottomAnchor, multiplier: 5.0)
                ])
            self.web1.scrollView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentBehavior.never
            self.web2.scrollView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentBehavior.never
            self.web3.scrollView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentBehavior.never
            
        } else {
            
        }
    }
    func SetListViews(){
        self.leftListView.frame = CGRect(x: self.web1.frame.origin.x + CGFloat(3), y: 50, width: self.web1.frame.size.width - 10 , height: 200)
        self.leftListView.delegate = self
        self.leftListView.dataSource = self
        self.view.addSubview(self.leftListView)
        self.leftListView.isHidden = true
        
        self.middleListView.frame = CGRect(x: self.web2.frame.origin.x + CGFloat(3), y: 50, width: self.web2.frame.size.width - 10 , height: 200)
        self.middleListView.delegate = self
        self.middleListView.dataSource = self
        self.view.addSubview(self.middleListView)
        self.middleListView.isHidden = true
        
        self.rightListView.frame = CGRect(x: self.web3.frame.origin.x + CGFloat(3), y: 50, width: self.web3.frame.size.width - 10 , height: 200)
        self.rightListView.delegate = self
        self.rightListView.dataSource = self
        self.view.addSubview(self.rightListView)
        self.rightListView.isHidden = true
    }
    func SetMiddleSwipeViews(){
        
        let viewarray = [self.web1, self.web2,self.web3] as [Any]
        let directions: [UISwipeGestureRecognizerDirection] = [.right, .left]
        for direction in directions {
            for views in viewarray{
                let gesture = UISwipeGestureRecognizer(target: self, action: #selector(LandScapeViewController.respondToMiddleSwipeGesture(_:)))
                gesture.direction = direction
                (views as AnyObject).addGestureRecognizer(gesture)
                if((views as AnyObject) as! NSObject == self.web1){
                    self.web1.scrollView.panGestureRecognizer.require(toFail: gesture)
                }
                else if((views as AnyObject) as! NSObject == self.web2){
                    self.web2.scrollView.panGestureRecognizer.require(toFail: gesture)
                }
                else{
                    self.web3.scrollView.panGestureRecognizer.require(toFail: gesture)
                }
            }
        }
    }
    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews();
        
        web1.scrollView.contentInset = UIEdgeInsets.zero
        web2.scrollView.contentInset = UIEdgeInsets.zero
        web3.scrollView.contentInset = UIEdgeInsets.zero
    }
    @objc func respondToMiddleSwipeGesture(_ gesture: UIGestureRecognizer) {
        
        if let swipeGesture = gesture as? UISwipeGestureRecognizer {
            
            switch swipeGesture.direction {
            case UISwipeGestureRecognizerDirection.right:
                if(self.web3.isHidden == false && self.web1.isHidden == false) {
                    
                    self.web1.frame.size.width = self.web1.frame.size.width + (self.web1.frame.size.width/2)
                    self.web2.frame.origin.x = self.web1.frame.origin.x + self.web1.frame.size.width
                    self.web2.frame.size.width =  self.web2.frame.size.width + (self.web2.frame.size.width/2)
                    self.web3.frame.origin.x =  self.web2.frame.origin.x + self.web2.frame.size.width
                    self.web3.frame.size.width = 0
                    
                    self.web3.isHidden = true
                    
                    self.feedLeftHeaderView.frame.size.width = self.feedLeftHeaderView.frame.size.width + (self.feedLeftHeaderView.frame.size.width/2)
                    self.feedMiddleHeaderView.frame.origin.x = self.feedLeftHeaderView.frame.origin.x + self.feedLeftHeaderView.frame.size.width
                    self.feedMiddleHeaderView.frame.size.width =  self.feedMiddleHeaderView.frame.size.width + ( self.feedMiddleHeaderView.frame.size.width/2)
                    self.feedRightHeaderView.frame.origin.x =  self.feedMiddleHeaderView.frame.origin.x + self.feedMiddleHeaderView.frame.size.width
                    self.feedRightHeaderView.frame.size.width = 0
                    self.feedRightHeaderView.isHidden = true
                }else if(self.web3.isHidden == false && self.web1.isHidden == true){
                    self.web1.frame.size.width = self.web2.frame.size.width - (self.web2.frame.size.width/3)
                    self.web2.frame.size.width = self.web2.frame.size.width - (self.web2.frame.size.width/3)
                    self.web2.frame.origin.x = self.web1.frame.origin.x + self.web1.frame.size.width
                    self.web3.frame.size.width = self.web3.frame.size.width - (self.web3.frame.size.width/3)
                    self.web3.frame.origin.x =  self.web2.frame.origin.x +  self.web2.frame.size.width
                    
                    self.web1.isHidden = false
                    self.feedLeftHeaderView.frame.size.width = self.feedMiddleHeaderView.frame.size.width - (self.feedMiddleHeaderView.frame.size.width/3)
                    self.feedMiddleHeaderView.frame.size.width = self.feedMiddleHeaderView.frame.size.width - (self.feedMiddleHeaderView.frame.size.width/3)
                    self.feedMiddleHeaderView.frame.origin.x = self.feedLeftHeaderView.frame.origin.x + self.feedLeftHeaderView.frame.size.width
                    self.feedRightHeaderView.frame.size.width = self.feedRightHeaderView.frame.size.width - (self.feedRightHeaderView.frame.size.width/3)
                    self.feedRightHeaderView.frame.origin.x =  self.feedMiddleHeaderView.frame.origin.x +  self.feedMiddleHeaderView.frame.size.width
                    
                    self.feedLeftHeaderView.isHidden = false
                }
            case UISwipeGestureRecognizerDirection.left:
                if(self.web3.isHidden == true && self.web1.isHidden == false) {
                    self.web1.frame.size.width = self.web1.frame.size.width - (self.web1.frame.size.width/3)
                    self.web2.frame.origin.x = self.web1.frame.origin.x + self.web1.frame.size.width
                    self.web2.frame.size.width = self.web2.frame.size.width - (self.web2.frame.size.width/3)
                    self.web3.frame.size.width = self.web2.frame.size.width
                    self.web3.frame.origin.x = self.web2.frame.origin.x + self.web2.frame.size.width
                    self.web3.isHidden = false
                    
                    self.feedLeftHeaderView.frame.size.width = self.feedLeftHeaderView.frame.size.width - (self.feedLeftHeaderView.frame.size.width/3)
                    self.feedMiddleHeaderView.frame.origin.x = self.feedLeftHeaderView.frame.origin.x + self.feedLeftHeaderView.frame.size.width
                    self.feedMiddleHeaderView.frame.size.width = self.feedMiddleHeaderView.frame.size.width - (self.feedMiddleHeaderView.frame.size.width/3)
                    self.feedRightHeaderView.frame.size.width = self.feedMiddleHeaderView.frame.size.width
                    self.feedRightHeaderView.frame.origin.x = self.feedMiddleHeaderView.frame.origin.x + self.feedMiddleHeaderView.frame.size.width
                    
                    self.feedRightHeaderView.isHidden = false
                }else if(self.web3.isHidden == false && self.web1.isHidden == false){
                    self.web2.frame.size.width = self.web2.frame.size.width + (self.web2.frame.size.width/2)
                    self.web2.frame.origin.x = self.web1.frame.origin.x
                    
                    self.web3.frame.size.width = self.web3.frame.size.width + (self.web3.frame.size.width/2)
                    self.web3.frame.origin.x = self.web2.frame.origin.x + self.web2.frame.size.width
                    
                    
                    self.web1.frame.size.width = 0
                    self.web1.isHidden = true
                    
                    self.feedMiddleHeaderView.frame.size.width = self.feedMiddleHeaderView.frame.size.width + (self.feedMiddleHeaderView.frame.size.width/2)
                    self.feedMiddleHeaderView.frame.origin.x = self.feedLeftHeaderView.frame.origin.x
                    
                    self.feedRightHeaderView.frame.size.width = self.feedRightHeaderView.frame.size.width + (self.feedRightHeaderView.frame.size.width/2)
                    self.feedRightHeaderView.frame.origin.x = self.feedMiddleHeaderView.frame.origin.x + self.feedMiddleHeaderView.frame.size.width
                    
                    
                    self.feedLeftHeaderView.frame.size.width = 0
                    self.feedLeftHeaderView.isHidden = true
                }
            default:
                break
            }
            
            self.fullviewfeedleft.frame.origin.x = self.feedLeftHeaderView.frame.size.width - 40
            self.fullviewfeedmiddle.frame.origin.x = self.feedMiddleHeaderView.frame.size.width - 40
            self.fullviewfeedright.frame.origin.x = self.feedRightHeaderView.frame.size.width - 40
            
            self.leftListView.frame.origin.x = self.web1.frame.origin.x + CGFloat(3)
            self.middleListView.frame.origin.x = self.web2.frame.origin.x + CGFloat(3)
            self.rightListView.frame.origin.x = self.web3.frame.origin.x + CGFloat(3)
            
        }
        
    }
    override var supportedInterfaceOrientations : UIInterfaceOrientationMask {
        let orientation: UIInterfaceOrientationMask = [UIInterfaceOrientationMask.landscapeLeft, UIInterfaceOrientationMask.landscapeRight]
        return orientation
        
    }
    override var preferredInterfaceOrientationForPresentation : UIInterfaceOrientation {
        if(self.orientationLSMId == "left"){
            
            return UIInterfaceOrientation.landscapeLeft
        }else {
            
            return UIInterfaceOrientation.landscapeRight
        }
        
    }
    
    override var shouldAutorotate : Bool {
        return false
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    override func viewDidAppear(_ animated: Bool) {
        NotificationCenter.default.addObserver(self, selector: #selector(LandScapeViewController.Rotated), name: NSNotification.Name.UIDeviceOrientationDidChange, object: nil)
    }
    override func viewDidDisappear(_ animated: Bool) {
        NotificationCenter.default.removeObserver(self, name:NSNotification.Name(rawValue: "UIDeviceOrientationDidChangeNotification"), object:nil)
    }
    @objc func Rotated(){
        if(UIDevice.current.orientation == UIDeviceOrientation.landscapeLeft || UIDevice.current.orientation == UIDeviceOrientation.landscapeRight){
        }else if(UIDevice.current.orientation == UIDeviceOrientation.portrait){
            self.dismiss(animated: false, completion: {
                if(self.maskView.isHidden == false){
                    webUrlString = self.fullOverlayWebView.tag
                    self.delegate?.GotoWebView?(self.fullOverlayWebView.stringByEvaluatingJavaScript(from: "window.location.href")!)
                }else{
                    webUrlString = 0
                    self.delegate?.GotoRootView?()
                }
            })
        }
        
    }
    @IBAction func CloseWebButtonAction(_ sender: UIButton) {
        self.maskView.isHidden = true
        self.fullOverlayWebView.tag = 0
    }
    @objc func FullViewFeedRightAction(_ sender: UIButton) {
        let urlString = self.web3.stringByEvaluatingJavaScript(from: "window.location.href")!
        self.fullOverlayWebView.tag = 3
        DispatchQueue.main.async(execute: {
            let requestURL = URL(string:urlString)
            if(requestURL != nil){
                let request = URLRequest(url: requestURL!)
                self.fullOverlayWebView.loadRequest(request)
            }else{
                let requestURL = URL(string:"https://www.google.com")
                if(requestURL != nil){
                    let request = URLRequest(url: requestURL!)
                    self.fullOverlayWebView.loadRequest(request)
                }
            }
            self.view.bringSubview(toFront: self.maskView)
        })
        self.maskView.isHidden = false
    }
    @objc func FilterFeedRightAction(_ sender: UIButton) {
        if(sender.isSelected == false){
            self.rightListView.isHidden = false
            sender.isSelected = true
        }else if(sender.isSelected == true){
            self.rightListView.isHidden = true
            sender.isSelected = false
        }
    }
    @objc func FullViewFeedMiddleAction(_ sender: UIButton) {
        let urlString = self.web2.stringByEvaluatingJavaScript(from: "window.location.href")!
        self.fullOverlayWebView.tag = 2
        DispatchQueue.main.async(execute: {
            let requestURL = URL(string:urlString)
            if(requestURL != nil){
                let request = URLRequest(url: requestURL!)
                self.fullOverlayWebView.loadRequest(request)
            }else{
                let requestURL = URL(string:"https://www.google.com")
                if(requestURL != nil){
                    let request = URLRequest(url: requestURL!)
                    self.fullOverlayWebView.loadRequest(request)
                }
            }
            self.view.bringSubview(toFront: self.maskView)
        })
        self.maskView.isHidden = false
    }
    @objc func FilterFeedMiddleAction(_ sender: UIButton) {
        if(sender.isSelected == false){
            self.middleListView.isHidden = false
            sender.isSelected = true
        }else if(sender.isSelected == true){
            self.middleListView.isHidden = true
            sender.isSelected = false
        }
    }
    @objc func FullViewFeedLeftAction(_ sender: UIButton) {
        let urlString = self.web1.stringByEvaluatingJavaScript(from: "window.location.href")!
        self.fullOverlayWebView.tag = 1
        DispatchQueue.main.async(execute: {
            let requestURL = URL(string:urlString)
            if(requestURL != nil){
                let request = URLRequest(url: requestURL!)
                self.fullOverlayWebView.loadRequest(request)
            }else{
                let requestURL = URL(string:"https://www.google.com")
                if(requestURL != nil){
                    let request = URLRequest(url: requestURL!)
                    self.fullOverlayWebView.loadRequest(request)
                }
            }
            self.view.bringSubview(toFront: self.maskView)
        })
        self.maskView.isHidden = false
    }
    @objc func FilterFeedLeftAction(_ sender: UIButton) {
        if(sender.isSelected == false){
            self.leftListView.isHidden = false
            sender.isSelected = true
        }else if(sender.isSelected == true){
            self.leftListView.isHidden = true
            sender.isSelected = false
        }
    }
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 7
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell{
        if(tableView == self.leftListView){
            var cell = tableView.dequeueReusableCell(withIdentifier: "leftcellid")
            
            if(cell == nil){
                cell = UITableViewCell(style: UITableViewCellStyle.default, reuseIdentifier: "leftcellid")
            }
            cell?.textLabel?.text = self.tempList[indexPath.row]
            cell?.selectionStyle = UITableViewCellSelectionStyle.none
            return cell!
        }else if(tableView == self.middleListView){
            var cell = tableView.dequeueReusableCell(withIdentifier: "middlecellid")
            
            if(cell == nil){
                cell = UITableViewCell(style: UITableViewCellStyle.default, reuseIdentifier: "middlecellid")
            }
            cell?.textLabel?.text = self.tempList[indexPath.row]
            cell?.selectionStyle = UITableViewCellSelectionStyle.none
            return cell!
        }else{
            var cell = tableView.dequeueReusableCell(withIdentifier: "rightcellid")
            
            if(cell == nil){
                cell = UITableViewCell(style: UITableViewCellStyle.default, reuseIdentifier: "rightcellid")
            }
            cell?.textLabel?.text = self.tempList[indexPath.row]
            cell?.selectionStyle = UITableViewCellSelectionStyle.none
            return cell!
        }
        
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat{
        return 40
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath){
        if(tableView == self.leftListView){
            if(indexPath.row != 6){
                if let url = URL(string: socialLinks[indexPath.row]) {
                    let request = URLRequest(url: url)
                    web1.loadRequest(request)
                }
            }
            self.leftListView.isHidden = true
            self.filterfeedleft.isSelected = false
        }else if(tableView == self.middleListView){
            if(indexPath.row != 6){
                if let url = URL(string: socialLinks[indexPath.row]) {
                    let request = URLRequest(url: url)
                    web2.loadRequest(request)
                }
            }
            self.middleListView.isHidden = true
            self.filterfeedmiddle.isSelected = false
        }else{
            if(indexPath.row != 6){
                if let url = URL(string: socialLinks[indexPath.row]) {
                    let request = URLRequest(url: url)
                    web3.loadRequest(request)
                }
            }
            self.rightListView.isHidden = true
            self.filterfeedright.isSelected = false
        }
    }
}
