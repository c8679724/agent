package org.objectweb.asm;

public class ClassWriter_ extends ClassWriter{

    /**
     * <tt>true</tt> if the maximum stack size and number of local variables
     * must be automatically computed.
     */
    private boolean computeMaxs;

    /**
     * <tt>true</tt> if the stack map frames must be recomputed from scratch.
     */
    private boolean computeFrames;
    
    /**
     * A reusable key used to look for items in the {@link #items} hash table.
     */
    final Item key;

    /**
     * A reusable key used to look for items in the {@link #items} hash table.
     */
    final Item key2;

    /**
     * A reusable key used to look for items in the {@link #items} hash table.
     */
    final Item key3;

    /**
     * A reusable key used to look for items in the {@link #items} hash table.
     */
    final Item key4;
    
    /**
     * The constant pool of this class.
     */
    final ByteVector pool;
    
    /**
     * The internal name of this class.
     */
    String thisName;
	
	 // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    /**
     * Constructs a new {@link ClassWriter} object.
     * 
     * @param flags
     *            option flags that can be used to modify the default behavior
     *            of this class. See {@link #COMPUTE_MAXS},
     *            {@link #COMPUTE_FRAMES}.
     */
    public ClassWriter_(final int flags) {
        super(Opcodes.ASM5);
        index = 1;
        pool = new ByteVector();
        items = new Item[256];
        threshold = (int) (0.75d * items.length);
        key = new Item();
        key2 = new Item();
        key3 = new Item();
        key4 = new Item();
        this.computeMaxs = (flags & COMPUTE_MAXS) != 0;
        this.computeFrames = (flags & COMPUTE_FRAMES) != 0;
    }

    /**
     * Constructs a new {@link ClassWriter} object and enables optimizations for
     * "mostly add" bytecode transformations. These optimizations are the
     * following:
     * 
     * <ul>
     * <li>The constant pool from the original class is copied as is in the new
     * class, which saves time. New constant pool entries will be added at the
     * end if necessary, but unused constant pool entries <i>won't be
     * removed</i>.</li>
     * <li>Methods that are not transformed are copied as is in the new class,
     * directly from the original class bytecode (i.e. without emitting visit
     * events for all the method instructions), which saves a <i>lot</i> of
     * time. Untransformed methods are detected by the fact that the
     * {@link ClassReader} receives {@link MethodVisitor} objects that come from
     * a {@link ClassWriter} (and not from any other {@link ClassVisitor}
     * instance).</li>
     * </ul>
     * 
     * @param classReader
     *            the {@link ClassReader} used to read the original class. It
     *            will be used to copy the entire constant pool from the
     *            original class and also to copy other fragments of original
     *            bytecode where applicable.
     * @param flags
     *            option flags that can be used to modify the default behavior
     *            of this class. <i>These option flags do not affect methods
     *            that are copied as is in the new class. This means that the
     *            maximum stack size nor the stack frames will be computed for
     *            these methods</i>. See {@link #COMPUTE_MAXS},
     *            {@link #COMPUTE_FRAMES}.
     */
    public ClassWriter_(final ClassReader classReader, final int flags) {
        this(flags);
        classReader.copyPool(this);
        this.cr = classReader;
    }
}
