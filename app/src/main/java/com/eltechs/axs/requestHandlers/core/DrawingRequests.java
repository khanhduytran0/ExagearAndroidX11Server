package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.proto.input.annotations.Width;
import com.eltechs.axs.proto.input.errors.BadMatch;
import com.eltechs.axs.proto.input.errors.BadValue;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.requestHandlers.IncomingImageFormat;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xconnectors.XResponse.ResponseDataWriter;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.GraphicsContext;
import com.eltechs.axs.xserver.Painter;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.graphicsContext.PixelCompositionRule;
import java.io.IOException;
import java.nio.ByteBuffer;
import com.eltechs.axs.proto.input.annotations.*;

public class DrawingRequests extends HandlerObjectBase {

    public enum CoordinateMode {
        ORIGIN,
        PREVIOUS
    }

    @RequestHandler(opcode = 67)
    @Locks({"DRAWABLES_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
    public void PolyRectangle(Drawable drawable, GraphicsContext graphicsContext, ByteBuffer byteBuffer) {
    }

    @RequestHandler(opcode = 66)
    @Locks({"DRAWABLES_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
    public void PolySegment(Drawable drawable, GraphicsContext graphicsContext, ByteBuffer byteBuffer) {
    }

    public DrawingRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 72)
    @Locks({"DRAWABLES_MANAGER", "WINDOWS_MANAGER", "PIXMAPS_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
	@OOBParam(index = 0)
	@Width(
		indexes = {3, 4, 5, 6},
		values = {2, 2, 2, 2}
	)
	@IntSign(
		signedIndexes = {5},
		unsignedIndexes = {3, 4, 6}
	)
    public void PutImage(IncomingImageFormat incomingImageFormat, Drawable drawable, GraphicsContext graphicsContext, int i, int i2, int i3, int i4, byte b, byte b2, short s, ByteBuffer byteBuffer) throws XProtocolError {
        IncomingImageFormat incomingImageFormat2 = incomingImageFormat;
        byte b3 = b2;
        Painter painter = drawable.getPainter();
        if (!(graphicsContext.getFunction() == PixelCompositionRule.COPY || incomingImageFormat2 == IncomingImageFormat.Z_PIXMAP)) {
            Assert.notImplementedYet("Drawing with GC::Function values other than COPY is not supported yet.");
        }
        switch (incomingImageFormat2) {
            case BITMAP:
                if (b != 0) {
                    Assert.notImplementedYet("PutImage.leftPad != 0 not implemented yet");
                }
                if (b3 != 1) {
                    throw new BadMatch();
                }
                painter.drawBitmap(i3, i4, i, i2, byteBuffer);
                return;
            case XY_PIXMAP:
                if (drawable.getVisual().getDepth() != b3) {
                    throw new BadMatch();
                }
                return;
            case Z_PIXMAP:
                if (drawable.getVisual().getDepth() == b3 && b == 0) {
                    painter.drawZPixmap(graphicsContext.getFunction(), b3, i3, i4, 0, 0, i, i2, byteBuffer, i, i2);
                    return;
                }
                throw new BadMatch();
            default:
                Assert.state(false, String.format("Unknown IncomingImageFormat %s.", new Object[]{incomingImageFormat2}));
                return;
        }
    }

    @RequestHandler(opcode = 73)
    @Locks({"DRAWABLES_MANAGER", "PIXMAPS_MANAGER"})
	@OOBParam(index = 1)
	@Width(
		indexes = {3, 4, 5, 6},
		values = {2, 2, 2, 2}
	)
	@IntSign(
		signedIndexes = {3, 4},
		unsignedIndexes = {5, 6}
	)
    public void GetImage(XResponse xResponse, IncomingImageFormat incomingImageFormat, Drawable drawable, int i, int i2, int i3, int i4, int i5) throws XProtocolError, IOException {
        final int i6;
        if (incomingImageFormat == IncomingImageFormat.BITMAP) {
            throw new BadValue(incomingImageFormat.ordinal());
        }
        Rectangle rectangle = new Rectangle(i, i2, i3, i4);
        if (!(this.xServer.getPixmapsManager().getPixmap(drawable.getId()) != null)) {
            i6 = drawable.getVisual().getId();
        } else if (!new Rectangle(0, 0, drawable.getWidth(), drawable.getHeight()).containsInnerRectangle(rectangle)) {
            throw new BadMatch();
        } else {
            i6 = 0;
        }
        Painter painter = drawable.getPainter();
        byte[] bArr = null;
        switch (incomingImageFormat) {
            case XY_PIXMAP:
                Assert.notImplementedYet("Reading data as XY Pixmap is unimplemented yet.");
                break;
            case Z_PIXMAP:
                bArr = painter.getZPixmap(i, i2, i3, i4);
                break;
            default:
                Assert.state(false, String.format("Unknown IncomingImageFormat %s.", new Object[]{incomingImageFormat}));
                break;
        }
		final byte[] bArrPut = bArr;
        xResponse.sendSuccessReplyWithPayload((byte) drawable.getVisual().getDepth(), new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putInt(i6);
            }
        }, bArr.length, new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.put(bArrPut);
            }
        });
    }

    @RequestHandler(opcode = 62)
    @Locks({"DRAWABLES_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
	@Width(
		indexes = {3, 4, 5, 6, 7, 8},
		values = {2, 2, 2, 2, 2, 2}
	)
	@IntSign(
		signedIndexes = {3, 4, 5, 6},
		unsignedIndexes = {7, 8}
	)
    public void CopyArea(Drawable drawable, Drawable drawable2, GraphicsContext graphicsContext, int i, int i2, int i3, int i4, int i5, int i6) {
        drawable2.getPainter().copyArea(graphicsContext, drawable, i, i2, i3, i4, i5, i6);
    }

    @RequestHandler(opcode = 61)
    @Locks({"WINDOWS_MANAGER"})
	@OOBParam(index = 0)
	@Width(
		indexes = {2, 3, 4, 5},
		values = {2, 2, 2, 2}
	)
	@IntSign(
		signedIndexes = {2, 3},
		unsignedIndexes = {4, 5}
	)
    public void ClearArea(Boolean bool, Window window, int i, int i2, int i3, int i4) {
        if (i3 != 0 || i4 != 0) {
            Assert.notImplementedYet("ClearArea is not implemented");
        }
    }

    @RequestHandler(opcode = 70)
    @Locks({"DRAWABLES_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
    public void PolyFillRectangle(Drawable drawable, GraphicsContext graphicsContext, ByteBuffer byteBuffer) {
        if (graphicsContext.getFunction() == PixelCompositionRule.COPY) {
            drawable.getPainter().drawFilledRectangles(byteBuffer, graphicsContext.getBackground());
        }
    }

    @RequestHandler(opcode = 65)
    @Locks({"DRAWABLES_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
	@OOBParam(index = 0)
    public void PolyLine(CoordinateMode coordinateMode, Drawable drawable, GraphicsContext graphicsContext, ByteBuffer byteBuffer) {
        if (coordinateMode == CoordinateMode.ORIGIN && graphicsContext.getLineWidth() == 1 && graphicsContext.getFunction() == PixelCompositionRule.COPY) {
            drawable.getPainter().drawLines(byteBuffer, graphicsContext.getForeground(), graphicsContext.getLineWidth());
        }
    }
}
