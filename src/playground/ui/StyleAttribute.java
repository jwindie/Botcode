package playground.ui;

class StyleAttribute<T> {

  T value;
  boolean set = false;
  
  StyleAttribute () {}

  T get () {return value;}

  void clear() {set = false;}

  void set (T value) {
    this.value = value;
    set = true;
  }
  
  StyleAttribute<T> copy() {
    StyleAttribute<T> copy = new StyleAttribute<>();
    
    copy.set = this.set;
    copy.value = cloneValue(value);
    
    return copy;
  }
  
  @SuppressWarnings("unchecked")
  private T cloneValue(T val) {
    if (val == null) return null;
    if (val.getClass().isArray()) {
      if (val instanceof float[]) return (T)((float[])val).clone();
      if (val instanceof int[])   return (T)((int[])val).clone();
      if (val instanceof Object[]) return (T)((Object[])val).clone();
    }
    return val;
  }
}