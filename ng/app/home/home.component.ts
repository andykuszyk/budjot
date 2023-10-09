import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
    menuItems: string[];

    constructor() { 
        this.menuItems = ['Menu item 1', 'Menu item 2', 'Spam', 'Eggs'];
    }
    
    ngOnInit() {}
}
