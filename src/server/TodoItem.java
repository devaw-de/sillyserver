package server;

class TodoItem {

  private int id;
  private int userId;
  private String title;
  private boolean completed;

  TodoItem(int userId, String title, boolean completed) {
    this.id = 0;
    this.userId = userId;
    this.title = title;
    this.completed = completed;
  }

  void toggleCompleted() {
    this.completed = !this.completed;
  }

}
