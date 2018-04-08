package _06_class_2;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
   mv = {1, 1, 9},
   bv = {1, 0, 2},
   k = 1,
   d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fHÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006"},
   d2 = {"L_06_class_2/JsonString;", "L_06_class_2/JsonValue;", "value", "", "(Ljava/lang/String;)V", "getValue", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString"}
)
public final class JsonString extends JsonValue {
   @NotNull
   private final String value;

   @NotNull
   public final String getValue() {
      return this.value;
   }

   public JsonString(@NotNull String value) {
      Intrinsics.checkParameterIsNotNull(value, "value");
      super((DefaultConstructorMarker)null);
      this.value = value;
   }

   @NotNull
   public final String component1() {
      return this.value;
   }

   @NotNull
   public final JsonString copy(@NotNull String value) {
      Intrinsics.checkParameterIsNotNull(value, "value");
      return new JsonString(value);
   }

   public String toString() {
      return "JsonString(value=" + this.value + ")";
   }

   public int hashCode() {
      return this.value != null ? this.value.hashCode() : 0;
   }

   public boolean equals(Object var1) {
      if (this != var1) {
         if (var1 instanceof JsonString) {
            JsonString var2 = (JsonString)var1;
            if (Intrinsics.areEqual(this.value, var2.value)) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }
}