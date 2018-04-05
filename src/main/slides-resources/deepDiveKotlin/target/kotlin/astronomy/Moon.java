package astronomy;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {1, 1, 9},
   bv = {1, 0, 2},
   k = 1,
   d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fHÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0003HÖ\u0001R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006"},
   d2 = {"Lastronomy/Moon;", "Lastronomy/AstronomicalBody;", "name", "", "(Ljava/lang/String;)V", "getName", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString"}
)
public final class Moon implements AstronomicalBody {
   @NotNull
   private final String name;

   @NotNull
   public String getName() {
      return this.name;
   }

   public Moon(@NotNull String name) {
      Intrinsics.checkParameterIsNotNull(name, "name");
      super();
      this.name = name;
   }

   @NotNull
   public final String component1() {
      return this.getName();
   }

   @NotNull
   public final Moon copy(@NotNull String name) {
      Intrinsics.checkParameterIsNotNull(name, "name");
      return new Moon(name);
   }

   public String toString() {
      return "Moon(name=" + this.getName() + ")";
   }

   public int hashCode() {
      String var10000 = this.getName();
      return var10000 != null ? var10000.hashCode() : 0;
   }

   public boolean equals(Object var1) {
      if (this != var1) {
         if (var1 instanceof Moon) {
            Moon var2 = (Moon)var1;
            if (Intrinsics.areEqual(this.getName(), var2.getName())) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }
}
