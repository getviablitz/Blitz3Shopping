//
//  WebViewController.swift
//  Blitz3Social
//
//  Created by Sivaprasad Masina on 25/07/18.
//  Copyright © 2018 BlitzIt. All rights reserved.
//

import UIKit

class WebViewController: UIViewController, UIWebViewDelegate, lsmviewdelegate {

    @IBOutlet var containerView: UIWebView!
    var urlString = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        self.navigationItem.title = "Web View"
        DispatchQueue.main.async(execute: {
            let requestURL = URL(string:self.urlString)
            if(requestURL != nil){
                let request = URLRequest(url: requestURL!)
                self.containerView.loadRequest(request)
            }else{
                let requestURL = URL(string:"https://www.google.com")
                
                if(requestURL != nil){
                    let request = URLRequest(url: requestURL!)
                    self.containerView.loadRequest(request)
                }
            }
        })
        
        let cancel = UIButton(type: UIButtonType.system)
        if(DeviceType.IS_IPHONE_5 || DeviceType.IS_IPHONE_4_OR_LESS){
            cancel.frame = CGRect(x: 0, y: 7, width: 25, height: 25)
        }else{
            cancel.frame = CGRect(x: 0, y: 7, width: 32, height: 32)
        }
        cancel.setBackgroundImage(UIImage(named: "backarrow.png"), for: UIControlState())
        cancel.addTarget(self, action: #selector(WebViewController.CancelButton(_:)), for: UIControlEvents.touchUpInside)
        let cancelItem:UIBarButtonItem = UIBarButtonItem(customView: cancel)
        self.navigationItem.setLeftBarButtonItems([cancelItem], animated: true)
    }
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.isNavigationBarHidden = false
        self.navigationController?.navigationBar.isHidden = false
        self.navigationItem.hidesBackButton = true
        if #available(iOS 11, *) {
            let guide = view.safeAreaLayoutGuide
            NSLayoutConstraint.activate([
                containerView.topAnchor.constraintEqualToSystemSpacingBelow(guide.topAnchor, multiplier: 0.0),
                guide.bottomAnchor.constraintEqualToSystemSpacingBelow(containerView.bottomAnchor, multiplier: 5.0)
                ])
            self.containerView.scrollView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentBehavior.never
            self.automaticallyAdjustsScrollViewInsets = false
            
        } else {
            
        }
    }
    override func viewDidAppear(_ animated: Bool) {
        NotificationCenter.default.addObserver(self, selector: #selector(WebViewController.Rotated), name: NSNotification.Name.UIDeviceOrientationDidChange, object: nil)
    }
    override func viewDidDisappear(_ animated: Bool) {
        NotificationCenter.default.removeObserver(self, name:NSNotification.Name(rawValue: "UIDeviceOrientationDidChangeNotification"), object:nil)
    }
    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews();
        
        containerView.scrollView.contentInset = UIEdgeInsets.zero
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @objc func CancelButton(_ sender : UIButton){
        let lsmViewControler = self.storyboard?.instantiateViewController(withIdentifier: "LSMId") as! LandScapeViewController
        lsmViewControler.orientationLSMId = "left"
        lsmViewControler.fromView = "web"
        lsmViewControler.webToUrlString = self.containerView.stringByEvaluatingJavaScript(from: "window.location.href")!
        lsmViewControler.delegate = self
        lsmViewControler.filterArray = socialMediaIndexes
        self.navigationController?.present(lsmViewControler, animated: true, completion: nil)
    }
    @objc func Rotated(){
        if(UIDevice.current.orientation == UIDeviceOrientation.landscapeLeft || UIDevice.current.orientation == UIDeviceOrientation.landscapeRight){
            let lsmViewControler = self.storyboard?.instantiateViewController(withIdentifier: "LSMId") as! LandScapeViewController
            if(UIDevice.current.orientation == UIDeviceOrientation.landscapeLeft){
                lsmViewControler.orientationLSMId = "right"
            }else if(UIDevice.current.orientation == UIDeviceOrientation.landscapeRight){
                lsmViewControler.orientationLSMId = "left"
            }
            if(DeviceType.IS_IPAD_MINI){
                lsmViewControler.fromSubView = "rotation"
            }
            lsmViewControler.fromView = "web"
            lsmViewControler.webToUrlString = self.containerView.stringByEvaluatingJavaScript(from: "window.location.href")!
            lsmViewControler.delegate = self
            lsmViewControler.filterArray = socialMediaIndexes
            self.navigationController?.present(lsmViewControler, animated: true, completion: nil)
        }else if(UIDevice.current.orientation == UIDeviceOrientation.portrait){
            
        }
    }
    func GotoRootView() {
        self.navigationController?.popToRootViewController(animated: false)
    }
    func GotoWebView(_ url: String) {
        self.urlString = url
    }
}
