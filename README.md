# Pre-work - ToDoLi

*ToDoLi* is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: *H. Kanekal*

Time spent:

## User Stories

The following **required** functionality is completed:

* [X] User can **successfully add and remove items** from the todo list
* [X] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [X] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [Using Table layout]
* [x] Add support for completion due dates for todo items (and display within listview item)
* [ ] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [x] Add support for selecting the priority of each todo item (and display in listview item)
* [x] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [x] List anything else that you can get done to improve the app functionality!
	1. Check that the date for the due-date is not in the past, and toast a message if a past date is selected for due date
	2. Ask user for comfirmation of deletion of a task
	3. Use a spinner for date selection, and set the spinner to todays date
	4. If user marks task as done, a strike through shows on the task in main display, user still needs to delete task if necessary
	5. Background color of column of priority changes based on {High,Medium,Low} => { Red, Green, Blue }
	6. Deliberately moved away from Fragments as the data passing mechanism between fragments is un-necessary overhead

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

.

<a href="http://imgur.com/nF39cwJ"><img src="http://i.imgur.com/nF39cwJ.gif" title="ToDoLi Walkthrough" /></a>

<a href="http://imgur.com/ICtybTz"><img src="http://i.imgur.com/ICtybTz.gif" title="ToDoLi Dynamic Column sizing" /></a>

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.
	Debugging takes time. 
	Views set up in layout XML files need to be handcrafted for complex layouts, but using the design tab is OK for a quick and simple layouts.
	Using the correct algorithms is important before adding spinner-listeners or on-click listeners etc.
## License

    Copyright [2017] [H. Kanekal]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
