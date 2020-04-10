package server;

class TodoItem {

  private int id;
  private int userId;
  private String title;
  private boolean completed;

  TodoItem(int id, int userId, String title, boolean completed) {
    this.id = id;
    this.userId = userId;
    this.title = title;
    this.completed = completed;
  }

  void toggleCompleted() {
    this.completed = !this.completed;
  }
}
