# IMDB Clone Project

This project is a Java-based clone of the IMDB platform, providing functionalities for users to view, add, and manage productions, actors, and user accounts. The application includes both GUI and terminal interfaces.

## Features

* **User Authentication**: Login and manage user accounts using `Login.java`,    `LoginGUI.java`, and `Credentials.java`.
* **User Management**: Admins can add or delete users using `ManageUsers.java` and `Admin.java`.
* **Productions Management**: View, add, update, or delete productions and actors with `ManageProductionsOrActors.java` and `Production.java`.
* **Requests System**: Users can create requests for new productions or features using `Requests.java` and `Request.java`.
* **Notifications**: Send and receive notifications with `Notification.java` and `IMDB.java` implementing the `Subject` interface.
* **Favorites Management**: Users can manage their favorite productions using `ManageFavorites.java`.
* **Reviews Management**: Users can add or view reviews for productions.

## Package Structure

* `org.example`
  + **Main Classes**
    - `IMDB.java`: Singleton class acting as the main database.
    - `User.java`: Basic user class.
    - `Admin.java`: Extends `Staff` and implements admin functionalities.
    - `Credentials.java`: Handles user credentials.
    - `Notification.java`: Represents notifications sent to users.
    - `Request.java`: Represents user requests.
    - `Genre.java`: Enum for production genres.
    - `Episode.java`: Represents an episode of a series.
  + **Interface and Abstract Classes**
    - `StaffInterface.java`: Interface for staff-related methods.
    - `Subject.java`: Interface for the observer pattern implementation.
  + **Utilities**
    - `JsonParser.java`: Handles JSON parsing for data storage.
    - `RequestsHolder.java`: Holds all user requests.
  + **GUI Components (org.example.gui)**
    - `LoginGUI.java`: Graphical login interface.
    - `MenuGUI.java`: Main menu GUI for user interaction.
    - `CreateNewRequestPanelGUI.java`: GUI panel for creating new requests.
    - `ManageReviewsPanelGUI.java`: GUI for managing reviews.
  + **Terminal Components (org.example.terminal)**
    - `Login.java`: Terminal-based login interface.
    - `MainPage.java`: Terminal-based main page after login.
    - `Menu.java`: Terminal menu for user actions.
    - `ManageUsers.java`: Terminal interface for user management.
    - `ManageProductionsOrActors.java`: Terminal interface to manage productions or actors.
    - `ViewProductions.java`: Terminal interface to view productions.
    - `ManageFavorites.java`: Terminal interface for managing favorites.
    - `Requests.java`: Terminal interface for handling user requests.
    - `UpdateProductionsOrActors.java`: Terminal interface for updating productions or actors.
    - `ViewNotifications.java`: Terminal interface to view notifications.

## Getting Started

1. **Prerequisites**: Ensure Java is installed on your machine.
2. **Clone the Repository**: `git clone [repository_url]`.
3. **Build the Project**: Use a build tool like Maven or Gradle if provided.
4. **Run the Application**: Execute the main class `IMDB.java`.

## Usage

* **Login**: Users can log in using their email and password.
* **Admin Actions**:
  + Add or remove users.
  + Approve or deny user requests.
* **User Actions**:
  + View and search productions.
  + Manage favorites list.
  + Create new requests.
  + View notifications.

## Data Persistence

Data is stored and managed using JSON files, with `JsonParser.java` handling reading and writing operations.

## License

This project is licensed under the MIT License.
