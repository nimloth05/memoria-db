package bootstrap.core;

public final class ObjectRepoFactory {
  
  public static ObjectRepo create() {
    ObjectRepo repo = new ObjectRepo();
    boostrap(repo);
    return repo;
  }
  
  
  private static void boostrap(ObjectRepo repo) {
    MetaClass classObject = new MetaClass(MetaClass.class);
    repo.put(MetaClass.METACLASS_OBJECT_ID, classObject);
  }

  private ObjectRepoFactory() {}

}
